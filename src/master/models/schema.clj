(ns master.models.schema
  (:require [master.models.db :refer :all]
            [clojure.java.jdbc :as sql]))

(defn create-users-table []
  (sql/with-connection db
    (sql/create-table
     :users
     [:id "varchar(32) PRIMARY KEY"]
     [:pass "varchar(100)"])))

;Metadata
(defn create-images-table []
  (sql/with-connection db
    (sql/create-table
     :images
     [:userid "varchar(32)"]
     [:name "varchar(100)"])))
