import SwiftUI
import SwiftData

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
    .modelContainer(for: ListItemModel.self)
	}
}
