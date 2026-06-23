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
  private(set) var type: Int
  var name: String

  init(type: Int, name: String = "") {
    self.type = type
    self.name = name
  }
}
