(ns master.handler
  (:require ;[compojure.core :refer [defroutes routes]]
            ;[ring.middleware.resource :refer [wrap-resource]]
            ;[ring.middleware.file-info :refer [wrap-file-info]]
            ;[hiccup.middleware :refer [wrap-base-url]]
            ;[compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [defroutes]]
            [noir.util.middleware :as noir-middleware]
            [master.routes.home :refer [home-routes]]
            [master.routes.auth :refer [auth-routes]]
            [master.routes.upload :refer [upload-routes]]
            [noir.session :as session]
            [master.routes.gallery :refer [gallery-routes]]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defn init []
  (println "master is starting"))

(defn destroy []
  (println "master is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn user-page [_]
  (session/get :user))

(def app (noir-middleware/app-handler
           [auth-routes
            home-routes
            upload-routes
            gallery-routes
            app-routes]
           :middleware [wrap-restful-format]
           :access-rules [user-page]))


