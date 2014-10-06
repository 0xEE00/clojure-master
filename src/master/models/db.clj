(ns master.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db
  {:subprotocol "postgresql"
   :subname "//localhost/master_rad"
   :user "admin"
   :password "admin"})

;(defn create-user [user]
  ;(sql/with-connection
    ;db
    ;(sql/insert-record :users user)))

;Vraca korisnika sa datim ID-em
;(defn get-user [id]
  ;(sql/with-connection
    ;db
    ;(sql/with-query-results
      ;res ["select * from users where id = ?" id] (first res))))

;Creating tempalte for entry database
(defmacro with-db [f & body]
`(sql/with-connection ~db (~f ~@body)))

(defn create-user [user]
  (with-db sql/insert-record :users user))

(defn get-user [id]
  (with-db sql/with-query-results
    res ["select * from users where id = ?" id] (first res)))

;Added metadata for image
(defn add-image [userid name]
  (with-db
    sql/transaction
    (if (sql/with-query-results
          res
          ["select userid from images where userid = ? and name = ?" userid name]
          (empty? res))
      (sql/insert-record :images {:userid userid :name name})
      (throw
       (Exception. "you have already uploaded an image with the same name")))))
;(defn get-user-data []
  ;(sql/with-connection db
    ;(sql/with-query-results res ["select * from users"] (println res))))

;Return all images filename by user
(defn images-by-user [userid]
  (with-db
    sql/with-query-results
    res ["select * from images where userid = ?" userid] (doall res)))

;Return image
(defn get-gallery-previews []
  (with-db
    sql/with-query-results
    res
    ["select * from
     (select *, row_number() over (partition by userid) as row_number from images)
     as rows where row_number = 1"]
    (doall res)))

(defn delete-image [userid name]
  (with-db
    sql/delete-rows :images ["userid=? and name=?" userid name]))

(defn delete-user [userid]
(with-db sql/delete-rows :users ["id=?" userid]))
