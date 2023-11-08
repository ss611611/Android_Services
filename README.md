# Service
## 什麼是Service？
* Service 不是一個單獨的 process。
* Service 不是 thread。
## Service 提供兩個主要功能：
* App 可以告訴 system 它想要在 background 執行的操作（即使用戶沒有直接與 App 互動）。
  這對應於對 Context.startService() 的調用，它要求系統安排服務的工作，運行直到服務或其他人明確停止它。
* App 向其他 App 公開其某些功能的工具。
  這對應於對 Context.bindService() 的調用，它允許與 Service 建立長期連接以便與其互動。
## 三種不同的服務類型：
* Foreground
  foreground service 執行一些使用者能注意到的操作。
* Background
  background service 執行使用者不會直接注意到的操作。
* Bound
  當 application component 透過呼叫bindService()綁定到 service 時，service 即處於綁定狀態。bound service 會提供 client-server 接口，以便元件與服務進行互動、傳送請求、接收結果，甚至是利用[進程間通訊(IPC)](https://zh.wikipedia.org/zh-tw/%E8%A1%8C%E7%A8%8B%E9%96%93%E9%80%9A%E8%A8%8A) 跨進程執行這些操作。
## Service 實作
* onStartCommand
  每次客戶端透過調用明確啟動 service 時由系統調用。Context.startService(Intent)
    ex.請動 Service 時，執行循環播放音樂功能。
* onDestroy
  由 system 呼叫以通知 service 不再使用並且正在刪除。
* onBind
  將 communication channel 回傳給 service。
## Service 生命週期
![image.png](https://hackmd.io/_uploads/SkvhgYOmp.png)

## Service 兩種啟動方式
### startService（啟動式服務）
* 透過呼叫startService()方法來啟動服務。
* 一旦啟動，該服務會無限期地運行，不受其他元件的生命週期影響。
* 該服務必須自己負責停止運行，通常透過呼叫stopSelf()方法。
* 其他元件也可以透過呼叫stopService()方法來強制停止該服務。
* 一旦服務停止，系統會將其銷毀，釋放資源。
### bindService（綁定式服務）
* 透過呼叫bindService()方法來建立與服務的連接。
* 客戶端（其他元件）通常使用[IBinder介面](https://developer.android.com/reference/android/os/IBinder)來與服務進行通訊，這使得客戶端能夠調用服務的方法和獲取數據。
* 可以有多個客戶端同時綁定到相同的服務。
* 當所有綁定的客戶端都呼叫unbindService()方法關閉連接後，系統會銷毀該服務。
* 這種方式不需要服務自己負責停止運行，而是由系統管理服務的生命週期。
### startService & bindService 差異
主要包括以下 5 個方面
* 觸發方式：
  啟動式服務是由其他元件（通常是Activity或BroadcastReceiver）呼叫startService()方法來觸發啟動。
  綁定式服務是由其他元件（通常是Activity）呼叫bindService()方法建立連接。
* 生命週期管理：
  啟動式服務無需與呼叫它的元件維持關聯，它運行獨立於呼叫它的元件生命週期。
  綁定式服務需要與呼叫它的元件保持連接，它的生命週期受到連接的客戶端的控制。
* 停止方式：
  啟動式服務必須由服務本身通過stopSelf()或其他元件通過stopService()明確停止。
  綁定式服務不需要自己停止，當所有綁定的客戶端都通過unbindService()解除綁定後，系統會自動停止服務。
* 通訊方式：
  啟動式服務通常使用Intent作為通訊機制，不提供直接的方法調用。
  綁定式服務使用IBinder介面來實現方法調用和數據交換，提供了更強大的互動能力。
* 多客戶端支持：
  啟動式服務通常無法同時處理多個呼叫，每次呼叫startService()都會觸發一個新的服務實例。
  綁定式服務可以支援多個客戶端同時綁定，並透過不同的客戶端來執行不同的操作。

使用情境
1. startService：
    * 獨立執行背景任務：如果你需要執行一個在背景中運行的獨立任務，無需與其他元件互動，則 startService 是一個合適的選擇。
    * 不需要連接和通訊：如果你的服務只是需要在後台執行，而不需要與其他元件進行通訊，那麼 startService 可能更適合。
1. bindService：
    * 需要與客戶端進行互動：如果你的服務需要與應用中的一個或多個客戶端（例如Activity）進行互動，收集數據或提供服務，那麼 bindService 是更適合的選擇。
    * 多個客戶端支援：如果你需要支援多個客戶端同時綁定到服務，並與其進行通訊，那麼 bindService 是更有彈性的。
## Service & IntentService
建立啟動服務（started service），可以擴展兩個類別來建立啟動服務：
* Service
  這是適用於所有服務的基底類別。擴展此類時，您必須建立用於執行所有服務工作的新線程，因為服務預設使用應用程式的主線程，這會降低應用正在運行的任何Activity 的效能。
* IntentService
  這是Service的子類，其使用工作執行緒逐一處理所有啟動請求。如果您不要求服務同時處理多個要求，此類為最佳選擇。實現onHandleIntent()，該方法會接收每個啟動請求的Intent，以便您執行後台工作。
## 參考資料
* [Service layer pattern](https://en.wikipedia.org/wiki/Service_layer_pattern)
* [Android Service](https://developer.android.com/guide/components/services?hl=zh-tw)
* [Services overview](https://developer.android.com/guide/components/services)
* [進程間通訊(IPC)](https://zh.wikipedia.org/zh-tw/%E8%A1%8C%E7%A8%8B%E9%96%93%E9%80%9A%E8%A8%8A)
* [IBinder介面](https://developer.android.com/reference/android/os/IBinder)
