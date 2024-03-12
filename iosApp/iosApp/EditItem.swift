//
//  EditItem.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-12.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct EditItem: View {
  @State var title = "Edit item"
  @State var input = ""

  var body: some View {
    Form {
      Section(title) {
        TextField("Name", text: $input)
        HStack {
          Spacer()
          Button("Cancel", role: .destructive) {}
          Button("Save" ) {}
            .buttonStyle(.borderedProminent)
        }
      }
    }
  }
}

#Preview {
  EditItem(input: "Sandra")
}
