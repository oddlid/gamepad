import SwiftUI

struct ContentView: View {
  @Environment(\.modelContext) var modelContext
  @State private var navPath = NavigationPath()
  @State private var sortOrder = [SortDescriptor(\ListItemModel.name)]
  @State private var searchText = ""

  var body: some View {
    NavigationStack(path: $navPath.animation(.easeInOut)) {
      GameListView(path: $navPath, searchString: searchText, sortOrder: sortOrder)
        .navigationTitle("Choose game")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
          Button("Add game", systemImage: "plus", action: addGame)
          Menu("Sort", systemImage: "arrow.up.arrow.down") {
            Picker("Sort", selection: $sortOrder) {
              Text("A-Z")
                .tag([SortDescriptor(\ListItemModel.name)])
              Text("Z-A")
                .tag([SortDescriptor(\ListItemModel.name, order: .reverse)])
            }
          }
        }
        .searchable(text: $searchText)
    }
  }

  func addGame() {
    let game = ListItemModel(type: ListItemType.game.rawValue, name: "", selected: false)
    modelContext.insert(game)
    navPath.append(game)
  }
}

#Preview {
  do {
    let previewer = try DataPreviewer()
    return ContentView()
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}

