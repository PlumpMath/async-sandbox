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

(defn event->msg [ev]
  {:type (keyword (.-type ev))
   :client-x (.-clientX ev)
   :client-y (.-clientY ev)})

(defn coords-str [msg]
  (str (:client-x msg) ", " (:client-y msg)))

(defn hook-coords-display!
  "Subscribe to event type and update element with every message."
  [p event-type el]
  (let [c (sub p event-type (chan))]
    (go (while true
          (let [msg (<! c)]
            (dom/setTextContent el (coords-str msg)))))))

(let [;; get a chan and perform event->msg transformation
      event-chan (->> (listen-chan (.-body js/document) [:mousedown :mousemove])
                      (map< event->msg))

      ;; create a pub
      p (pub event-chan :type)]

  ;; two go loops feed events from two different subscriptions
  (hook-coords-display! p :mousedown (dom/getElement "click-coords"))
  (hook-coords-display! p :mousemove (dom/getElement "mouse-coords")))
