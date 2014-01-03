(ns async-sandbox.pubsub.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [chan put! <! map< map> mult pub sub tap untap]]))

;; This file demonstrates the new pub/sub functionality of core.async

(defn listen-chan
  "Create a channel to which all events of specified types are fed."
  [el types]
  (if-not (coll? types)
    (listen-chan el [types])
    (let [c (chan)]
      (doseq [t types] (events/listen el (name t) #(put! c %)))
      c)))

(defn event-msg [ev]
  {:type (keyword (.-type ev))
   :client-x (.-clientX ev)
   :client-y (.-clientY ev)})

(defn coords-str [msg]
  (str (:client-x msg) ", " (:client-y msg)))

(let [event-chan (->> (listen-chan (.-body js/document) [:mousedown :mousemove])
                      (map< event-msg))
      p (pub event-chan :type) ;; create a pub
      click-chan (chan)
      mouse-chan (chan)
      click-coords (dom/getElement "click-coords")
      mouse-coords (dom/getElement "mouse-coords")]

  ;; subscribe two different channels on the same pub with two
  ;; different topics
  (sub p :mousedown click-chan)
  (sub p :mousemove mouse-chan)

  ;; two go loops feed events from two different subscriptions
  (go (while true
        (let [msg (<! click-chan)]
          (dom/setTextContent click-coords (coords-str msg)))))
  (go (while true
        (let [msg (<! mouse-chan)]
          (dom/setTextContent mouse-coords (coords-str msg))))))
