//
//  GameRoundView.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct GameRoundView: View {
  let gameName: String
  let players: Set<ListItemModel>

  var body: some View {
    Text(gameName)
    List(Array(players))  { player in
      Text(player.name)
    }
  }
}

//#Preview {
//  GameRoundView()
//}
