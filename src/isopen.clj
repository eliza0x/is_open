(ns isopen
  (:gen-class)
  (:require [org.httpkit.client :as http])
  (:require [clj-slack-client.core :as slack])
  (:require [clj-slack-client.rtm-transmit :as chat]))

;; サーバーとチャンネルの設定-----
(def api_token (System/getenv "SLACK_TOKEN"))
(def channel_id "C55V4MN3S") 
(def sensor_server_addres "http://192.168.2.237")
;--------------------------------

;; 返答の設定--------------------
(def  sensor_is_dead   "センサーが死んでるよ＞＜")
(def  sensor_not_found "センサーが見つからないよ＞＜")
(def  network_error    "通信ができないよ＞＜")
(def  no_maching       "なんかバグってるよ＞＜")
(def  reset            "おひるねするよ＞＜")
(def  restart          "おひるねするよ＞＜")
;--------------------------------

(defn post_slack [text] ; post to slack
  (chat/say-message channel_id text))

(defn restart_sensor_server []
  (http/get (clojure.string/join [sensor_server_addres "/restart"]))
  (post_slack restart))

(defn reset_sensor_server []
  (http/get (clojure.string/join [sensor_server_addres "/reset"]))
  (post_slack reset))

(defn is_open? [] ; センサーと通信、開閉を確認
  (let [res (http/get sensor_server_addres)]
    (cond
      (= (:status @res) nil) {:status :not_found :text (:error @res)}
      (= (:status @res) 200) {:status :success 
                              :is_open (if (= "true" (:body @res)) true false)}
      :else {:status :no_maching :text (:status @res)})))

(defn sensor_handle [s] ; センサーから帰って来たデータを処理
  (post_slack
    (case (s :status)
      :sensor_is_dead sensor_is_dead
      :not_found      sensor_not_found 
      :success        (if (s :is_open) "部室は空いてるよ＞＜"
                                       "部室は閉まってるよ＞＜")
      :no_maching     (clojure.string/join no_maching (:status s)
      no_maching))))

(defn responce [reply] ; それっぽいデータがきたらルーティング
  (cond
    (java.lang.String/.contains reply "部室") (sensor_handle (is_open?))
    (java.lang.String/.contains reply "リセット") (reset_sensor_server)
    (java.lang.String/.contains reply "再起動") (restart_sensor_server)
    (java.lang.String/.contains reply ":alisa:") (post_slack ":alisa:")
    :else (post_slack "ふぇぇ＞＜")))

(defn handle_slack_event ; eventがあると呼びだし
  [event]
  (when (and (= (event :type) "desktop_notification")
             (= (event :channel channel_id)))
    (future (responce (event :content)))))

(defn -main
  [& args]
  (slack/connect api_token handle_slack_event))

