(ns master.routes.home
  (:require [compojure.core :refer :all]
            [master.views.layout :as layout]
            [noir.session :as session]
            [master.routes.gallery :refer [show-galleries]]))

(defn home []
  (layout/common (show-galleries)))

(defroutes home-routes
  (GET "/" [] (home)))
