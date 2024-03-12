//
//  EditableListItem.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-12.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct EditableListItem: View {
  @State var leadingIcon = "list.clipboard"
  @State var value = "Item"
  @State var selected = false

  var body: some View {
    HStack {
      Image(systemName: leadingIcon)
      Text(value)
      Spacer()
      if selected {
        Image(systemName: "checkmark.circle")
      }
      Image(systemName: "pencil")
      Image(systemName: "trash")
    }
    .padding()
  }
}

#Preview {
  EditableListItem(
    leadingIcon: "person",
    value: "Sandra",
    selected: true
  )
}
