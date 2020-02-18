(ns queueue.views
  (:require [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]))

(defn status
  "Returns the given person's status string"
  [name queue]
  (let [[current & others] queue]
    (cond
      (empty? name)
      "You might want to enter your name first"
      (nil? current)
      "Is anyone here?"
      (= current name)
      "Your turn!"
      (some #(= % name) others)
      "Await your turn"
      :else
      "You're not in the queue")))

(defn action
  "Returns the given person's available action"
  [name queue]
  (let [[current & others] queue]
    (cond
      (= current name)
      "Done talking!"
      (some #(= % name) others)
      "Leave the queue"
      :else
      "Enter the queue")))

(defn status-display
  "Displays the user's current status in the queue"
  []
  (let [queue (re-frame/subscribe [:queue])
        name (re-frame/subscribe [:name])]
    [:h1 (status @name @queue)]))

(defn the-button []
  (let [queue (re-frame/subscribe [:queue])
        name (re-frame/subscribe [:name])]
    ; TODO: make the magic happen
    [:button {:class "the-button"} (action @name @queue)]))

(defn queue []
  (let [queue (re-frame/subscribe [:queue])]
    (into [:ul] (for [person @queue] [:li {:key person} person]))))

(defn name-input []
  (let [name (re-frame/subscribe [:name])]
    [:div {:class "name-input"}
     [:input {:type "string"
              :value @name
              :on-change #(re-frame/dispatch [:name-changed (-> % .-target .-value)])}]]))

(defn debug-display []
  (when js/goog.DEBUG
    [:div {:class "debug"}
      [:hr]
      [:h3 "Debugging: app state"]
      [:pre (with-out-str (pprint/pprint @(re-frame/subscribe [:db])))]]))

(defn main-panel []
  [:div
   [status-display]
   [:div [name-input]]
   [the-button]
   ; [one-mere-question]
   [queue]
   [debug-display]])
