//
//  GameListItemView.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-20.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct GameListItemView: View {
  @Binding var navPath: NavigationPath
  @Bindable var game: ListItemModel

  var body: some View {
    HStack {
      GameItemButton(text: game.name, iconSystemName: "list.clipboard")
        .onTapGesture {
          navPath.append(game.name)
        }
      EditItemButton(label: "Edit")
        .onTapGesture {
          navPath.append(game)
        }
    }
    .navigationDestination(for: ListItemModel.self) { game in
      EditListItemView(item: game)
    }
    .navigationDestination(for: String.self) { name in
      PlayerListView(path: $navPath, gameName: name)
    }
  }
}

struct EditItemButton: View {
  let label: String

  var body: some View {
    HStack {
      Image(systemName: "pencil")
      Text(label)
    }
//    .background(Color.blue)
  }
}

struct GameItemButton: View {
  let text: String
  let iconSystemName: String

  var body: some View {
    HStack {
      Image(systemName: iconSystemName)
      Text(text)
      Spacer()
    }
    .background(.background) // needed for the whole area to react to click
  }
}

#Preview {
  do {
    let previewer = try DataPreviewer()
    @State var path = NavigationPath()
    return GameListItemView(navPath: $path, game: previewer.games[0])
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}
