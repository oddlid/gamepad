//
//  EditListItemView.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-19.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftData
import SwiftUI

struct EditListItemView: View {
  @Environment(\.modelContext) var modelContext
  @Bindable var item: ListItemModel

  var body: some View {
      Form {
        Section("Edit name") {
          TextField("Name", text: $item.name)
            .textContentType(.name)
        }
      }
      .navigationTitle("Edit item")
      .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
  do {
    let previewer = try DataPreviewer()
    let item = previewer.games[0]
    return EditListItemView(item: item)
      .modelContainer(previewer.container)
  } catch {
    return Text("Failed to create preview: \(error.localizedDescription)")
  }
}
