(ns master.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [master.routes.home :refer :all]
            [master.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [master.models.db :as db]
            [master.util :refer [gallery-path]]
            [noir.util.route :refer [restricted]]
            [master.routes.upload :refer [delete-image]])
  (:import java.io.File))

(defn create-gallery-path []
  (let [user-path (File. (gallery-path))]
    (if-not (.exists user-path) (.mkdirs user-path))
    (str (.getAbsolutePath user-path) File/separator)))

(defn valid? [id pass pass1]
  (vali/rule (vali/has-value? id)
             [:id "user id is required"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass "entered passwords do not match"])
  (not (vali/errors? :id :pass :pass1)))

;Greska koja se prikazuje ako pokusavamo da reigisrujemo vec postojeceg korisnika
(defn format-error [id ex]
  (cond
   (and (instance? org.postgresql.util.PSQLException ex)
        (= 0 (.getErrorCode ex)))
   (str "The user with id " id " already exists!")
   :else
   "An error has occured while processing the request"))

;Kada polja nisu dobro popunjena ispisuje se obavestenje crvenim slovima
(defn error-item [[error]]
  [:div.error error])

;Kontrola koju koristimo u formi ispod. Refaktorisanje koda.
(defn control [id label field]
  (list
   (vali/on-error id error-item)
   label field
   [:br]))

(defn registration-page [& [id]]
  (layout/base
   (form-to [:post "/register"]
            (control :id
                     (label "user-id" "user id")
                     (text-field {:tabindex 1} "id" id))
            (control :pass
                     (label "pass" "password")
                     (password-field {:tabindex 2} "pass"))
            (control :pass1
                     (label "pass1" "retype password")
                     (password-field {:tabindex 3} "pass1"))
            (submit-button {:tabindex 4} "create account"))))

;Poziva se na dugme. Ako su podaci ispravno uneti pokusava da unese korisnika.
(defn handle-registration [id pass pass1]
  (if (valid? id pass pass1)
    (try
      (db/create-user {:id id :pass (crypt/encrypt pass)})
      (session/put! :user id)
      (create-gallery-path)
      (resp/redirect "/")
      (catch Exception ex
        (vali/rule false [:id (format-error id ex)])
        (registration-page)))
    (registration-page id)))

;Proverava ID i PASS, ako je OK loguje korisnika
(defn handle-login [id pass]
  (let [user (db/get-user id)]
    (if (and user (crypt/compare pass (:pass user)))
      (session/put! :user id)))
  (resp/redirect "/"))

;LOGOUT
(defn handle-logout []
  (session/clear!)
  (resp/redirect "/"))

;PRvi brise sve slike za korisnika a onda i samog korisnika
(defn handle-confirm-delete []
  (let [user (session/get :user)]
    (doseq [{:keys [name]} (db/images-by-user user)]
      (delete-image user name))
    (clojure.java.io/delete-file (gallery-path))
    (db/delete-user user))
  (session/clear!)
  (resp/redirect "/"))

(defn delete-account-page []
  (layout/common
   (form-to [:post "/confirm-delete"]
            (submit-button "delete account"))
   (form-to [:get "/"]
            (submit-button "cancel"))))

(defroutes auth-routes
  (GET "/register" []
       (registration-page))

  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1))

  (POST "/login" [id pass]
        (handle-login id pass))

  (GET "/logout" []
       (handle-logout))
  (GET "/delete-account" []
       (restricted (delete-account-page)))
  (POST "/confirm-delete" []
        (restricted (handle-confirm-delete))))
