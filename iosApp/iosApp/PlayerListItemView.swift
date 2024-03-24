//
//  EditableListItem.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-12.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct PlayerListItemView: View {
  @Binding var navPath: NavigationPath
  @Bindable var player: ListItemModel

  var body: some View {
    HStack {
      PlayerItemButton(player: player, iconSystemName: "person")
      EditItemButton(label: "Edit")
        .onTapGesture {
          navPath.append(player)
        }
    }
    .navigationDestination(for: ListItemModel.self) { player in
      EditListItemView(item: player)
    }
  }
}

struct PlayerItemButton: View {
  @Bindable var player: ListItemModel
  let iconSystemName: String

  var body: some View {
    HStack {
//      Toggle(isOn: $player.selected) {
//        Image(systemName: iconSystemName)
//        Text(player.name)
//      }
//      .toggleStyle(PlayerToggleStyle())
      Image(systemName: iconSystemName)
      Text(player.name)
      Spacer()
    }
  }
}

struct PlayerToggleStyle: ToggleStyle {
  func makeBody(configuration: Configuration) -> some View {
    Button {
      configuration.isOn.toggle()
    } label: {
      HStack {
        configuration.label
        Spacer()
        Image(systemName: configuration.isOn ? "checkmark" : "")
          .tint(configuration.isOn ? .green : .gray)
      }
    }
    .tint(.primary)
    .buttonStyle(.borderless)
  }
}

#Preview {
  do {
    let previewer = try DataPreviewer()
    @State var path = NavigationPath()
    return PlayerListItemView(navPath: $path, player: previewer.players[0])
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}
