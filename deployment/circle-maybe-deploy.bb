#!/usr/bin/env bb

(require '[clojure.java.shell :refer [sh]])
(require '[clojure.string :as str])

(def release-marker "Release-")

(defn make-version! [tag]
  (str/replace-first tag release-marker ""))

(when-let [tag (System/getenv "CIRCLE_TAG")]
  (when (re-find (re-pattern release-marker) tag)
    (apply println "Executing" *command-line-args*)
    (->> [:env (into {"PROJECT_VERSION" (make-version! tag)} (System/getenv))]
         (into (vec *command-line-args*))
         (apply sh)
         :exit
         (System/exit))))
