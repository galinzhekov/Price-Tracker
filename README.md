# Real-Time Price Tracker

Real-Time Price Tracker is a modern Android application built entirely with Jetpack Compose, designed to display live price updates for a list of stock symbols. The app demonstrates a clean, scalable, multi-module architecture following MVI (Model-View-Intent) principles.

This project serves as a comprehensive showcase of modern Android development, including dependency injection with Hilt, reactive data streams using Kotlin Flow, declarative UI, robust navigation patterns, and unit testing.

## ✨ Features

• **Live Price Feed:** Displays a scrollable list of 25 stock symbols with real-time price updates.  
• **WebSocket Integration:** Connects to `wss://ws.postman-echo.com/raw` to simulate a live data feed. Every 2 seconds, the app sends a random price update for each stock and processes the echoed response.  
• **Real-Time UI Updates:** UI reacts instantly to new price data, including a color-coded price change indicator (↑/↓).  
• **Dynamic Sorting:** Stocks automatically sort by price in descending order.  
• **Price Flash Animation:** Prices briefly flash green/red for 1 second on updates.  
• **Master–Detail Flow:** Tapping a stock symbol opens its details screen with live updates.  
• **Connection & Feed Control:** Top bar shows WebSocket connection status (🟢 Live / 🔴 Offline) and a toggle to start/stop the feed.  
• **Deep Linking:** Supports deep links in the format `stocks://symbol/{symbol}`  
  Example: `stocks://symbol/NVDA`

---

## 🏛️ Architecture & Tech Stack

This project utilizes a modern tech stack and follows clean architecture principles.

### **Architecture**

- **Multi-Module Architecture**  
  Modules: `app`, `core-network`, `core-navigation`, `feature-tracker`  
  Promotes separation, scalability, and faster builds.

- **MVI (Model-View-Intent)**  
  - Immutable `State` per screen  
  - `Events` for user actions  
  - Unidirectional data flow

- **Clean Architecture**  
  - **Presentation:** Jetpack Compose UI + ViewModels (`FeedViewModel`, `DetailsViewModel`)  
  - **Domain:** Business logic, models (`Stock`), repository interfaces  
  - **Data:** Repository implementations (`PriceTrackerRepositoryImpl`) and WebSocket data sources (`WebSocketService`)

### **Core Technologies**

- Kotlin  
- Coroutines & Flow  
- Hilt (Dependency Injection)

### **UI**

- Jetpack Compose  
- Material 3  
- Compose Navigation  
- ViewModel & SavedStateHandle  

### **Networking**

- Ktor Client (WebSocket communication)  
- Gson (serialization/deserialization)

### **Testing**

- JUnit  
- OkHttp MockWebServer (WebSocket testing)
