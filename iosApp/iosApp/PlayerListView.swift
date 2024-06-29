//
//  PlayerListView.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-20.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftData
import SwiftUI

struct PlayerListView: View {
  @Environment(\.modelContext) var modelContext
  @Binding var navPath: NavigationPath
  let gameName: String
  @State private var sortOrder = [SortDescriptor(\ListItemModel.name)]
  @State private var searchText = ""
  @State private var selection = Set<ListItemModel>()

  var body: some View {
    PlayerListContainer(path: $navPath, selection: $selection, searchString: searchText, sortOrder: sortOrder)
      .navigationTitle("Choose players")
      .navigationBarTitleDisplayMode(.inline)
      .toolbar {
        Button("Add player", systemImage: "plus", action: addPlayer)
        Button("Play", systemImage: "play.circle", action: {  })
          .disabled(selection.isEmpty)
        Menu("More", systemImage: "ellipsis") {
          Button("Delete", systemImage: "trash", action: deleteSelectedPlayers)
            .disabled(selection.isEmpty)
          Picker("Sort", selection: $sortOrder) {
            Text("A-Z")
              .tag([SortDescriptor(\ListItemModel.name)])
            Text("Z-A")
              .tag([SortDescriptor(\ListItemModel.name, order: .reverse)])
          }
        }
      }
      .searchable(text: $searchText)
    Text("\(selection.count) players selected") // TODO: remove, only for debug
  }

  init(path: Binding<NavigationPath>, gameName: String) {
    self._navPath = path
    self.gameName = gameName
  }

  func addPlayer() {
    let player = ListItemModel(type: ListItemType.player.rawValue, name: "")
    modelContext.insert(player)
    navPath.append(player)
  }

  func deleteSelectedPlayers() {
    for player in selection {
      modelContext.delete(player)
    }
    selection.removeAll()
  }
}

struct PlayerListContainer: View {
  @Environment(\.modelContext) var modelContext
  @Binding var navPath: NavigationPath
  @Query var players: [ListItemModel]
  @State private var editMode: EditMode = .transient
  @Binding var selection: Set<ListItemModel>

  var body: some View {
    List(selection: $selection) {
      ForEach(players, id: \.self) { player in
        PlayerListItemView(navPath: $navPath, player: player)
          .tag(player)
      }
//      .onDelete(perform: deletePlayers)
    }
    .environment(\.editMode, $editMode)
  }

  init(path: Binding<NavigationPath>, selection: Binding<Set<ListItemModel>>, searchString: String = "", sortOrder: [SortDescriptor<ListItemModel>] = []) {
    self._navPath = path
    self._selection = selection
    let playerType = ListItemType.player.rawValue
    _players = Query(filter: #Predicate<ListItemModel> {
      $0.type == playerType && searchString.isEmpty ? true : $0.name.localizedStandardContains(searchString)
    }, sort: sortOrder)
  }

//  func deletePlayers(at offsets: IndexSet) {
//    for offset in offsets {
//      let player = players[offset]
//      modelContext.delete(player)
//    }
//  }
}

#Preview {
//  PlayerListView(gameName: "Poker")
  do {
    let previewer = try DataPreviewer()
    @State var navPath = NavigationPath()
    return PlayerListView(path: $navPath, gameName: "Nåt spel")
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}
