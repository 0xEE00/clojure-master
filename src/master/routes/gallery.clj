(ns master.routes.gallery
  (:require [compojure.core :refer :all]
            [hiccup.element :refer :all]
            [master.views.layout :as layout]
            [master.util :refer [thumb-prefix image-uri thumb-uri]]
            [master.models.db :as db]
            [noir.session :as session]
            [hiccup.form :refer [check-box]]
            [hiccup.page :refer :all]))

;Linked to image
(defn thumbnail-link [{:keys [userid name]}]
  [:div.thumbnail
   [:a {:class name :href (image-uri userid name)}
    (image (thumb-uri userid name))
    (if (= userid (session/get :user)) (check-box name))]])

;Show gallery and delete option
(defn display-gallery [userid]
  (if-let [gallery (not-empty (map thumbnail-link (db/images-by-user userid)))]
    [:div
     [:div#error]
     gallery
     (if (= userid (session/get :user))
       [:input#delete {:type "submit" :value "delete images"}])]
    [:p "The user " userid " does not have any galleries"]))

;Linged on gallery page
(defn gallery-link [{:keys [userid name]}]
  [:div.thumbnail
   [:a {:href (str "/gallery/" userid)}
    (image (thumb-uri userid name))
    userid "'s gallery"]])

;Show all iamges for user
(defn show-galleries []
  (map gallery-link (db/get-gallery-previews)))

(defroutes gallery-routes
  (GET "/gallery/:userid" [userid]
       (layout/common
        (include-js "/js/gallery.js")
        (display-gallery userid))))
