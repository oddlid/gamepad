//
//  DataPreviewer.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-13.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftData

@MainActor
struct DataPreviewer {
  let container: ModelContainer
  let games: [ListItemModel]
  let players: [ListItemModel]

  init() throws {
    let config = ModelConfiguration(isStoredInMemoryOnly: true)
    container = try ModelContainer(for: ListItemModel.self, configurations: config)

    let tg = ListItemType.game.rawValue
    let tp = ListItemType.player.rawValue

    games = [
      ListItemModel(type: tg, name: "10.000"),
      ListItemModel(type: tg, name: "Poker"),
      ListItemModel(type: tg, name: "Finn röven"),
      ListItemModel(type: tg, name: "Bajs"),
      ListItemModel(type: tg, name: "Tudel"),
      ListItemModel(type: tg, name: "Häst"),
    ]

    players = [
      ListItemModel(type: tp, name: "Sandra"),
      ListItemModel(type: tp, name: "Sabina"),
      ListItemModel(type: tp, name: "Odd"),
      ListItemModel(type: tp, name: "Ubbe"),
      ListItemModel(type: tp, name: "Jimmy"),
      ListItemModel(type: tp, name: "Tobbe"),
      ListItemModel(type: tp, name: "Klas"),
      ListItemModel(type: tp, name: "Anna"),
      ListItemModel(type: tp, name: "Gunilla"),
      ListItemModel(type: tp, name: "Gustavo"),
      ListItemModel(type: tp, name: "Bubbis"),
      ListItemModel(type: tp, name: "Maria"),
    ]

    for game in games {
      container.mainContext.insert(game)
    }
    for player in players {
      container.mainContext.insert(player)
    }
  }
}
