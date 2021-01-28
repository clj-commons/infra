#!/usr/bin/env bb

;; Local Variables:
;; mode: Clojure
;; End:

(require '[clojure.java.shell :refer [sh]])
(require '[clojure.edn :as edn])
(require '[clojure.string :as str])


(defn all-good? []
  (zero? (:exit (sh "git" "diff" "--quiet"))))

(defn tag! [tag]
  (sh "git" "tag" tag))

(defn push! []
  (sh "git" "push"))

(defn push-tag! [tag]
  (sh "git" "push" "origin" tag))

(defn commit-count! []
  (str/trim-newline (:out (sh "git" "rev-list" "--count" "--first-parent" "HEAD"))))

(defn version! []
  (str/trim-newline (edn/read-string (slurp "version.edn"))))

(defn commit-count-version [version commit-count]
  (str version "." commit-count))

(defn release-tag [version]
  (str "Release-" version))

(if (all-good?)
  (let [version (commit-count-version (version!) (commit-count!))
        tag (release-tag version)]
    (println "Working-directory is clean, releasing" version)
    (push!)
    (tag! tag)
    (push-tag! tag)
    (println "done"))
  (println "unclean"))
