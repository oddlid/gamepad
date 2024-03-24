//
//  ListItem.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-13.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftData

enum ListItemType: Int {
  case game, player
}

@Model
class ListItemModel {
  let type: Int
  var name: String
  var selected: Bool

  init(type: Int, name: String = "", selected: Bool = false) {
    self.type = type
    self.name = name
    self.selected = selected
  }
}
