//
//  GameListView.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-19.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftData
import SwiftUI

struct GameListView: View {
  @Environment(\.modelContext) var modelContext
  @Query var games: [ListItemModel]
  @Binding var navPath: NavigationPath

  var body: some View {
    List {
      ForEach(games) { game in
        GameListItemView(navPath: $navPath, game: game)
      }
      .onDelete(perform: deleteGames)
    }
  }

  init(path: Binding<NavigationPath>, searchString: String = "", sortOrder: [SortDescriptor<ListItemModel>] = []) {
    self._navPath = path
    let gameType = ListItemType.game.rawValue
    _games = Query(filter: #Predicate<ListItemModel> {
      $0.type == gameType && searchString.isEmpty ? true : $0.name.localizedStandardContains(searchString)
    }, sort: sortOrder)
  }

  func deleteGames(at offsets: IndexSet) {
    for offset in offsets {
      let game = games[offset]
      modelContext.delete(game)
    }
  }

}

#Preview {
  do {
    let previewer = try DataPreviewer()
    @State var navPath = NavigationPath()
    return GameListView(path: $navPath)
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}
