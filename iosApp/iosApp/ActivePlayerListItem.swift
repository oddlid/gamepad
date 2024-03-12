//
//  ActivePlayerListItem.swift
//  iosApp
//
//  Created by Odd Eivind Ebbesen on 2024-03-12.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ActivePlayerListItem: View {
  @State var input = ""
  @State var points = 100
  @FocusState private var inputFocused: Bool

  var body: some View {
    VStack {
      HStack {
        Button {} label: {
          Image(systemName: "arrowtriangle.right")
        }
        PointInputField()
      }
    }
  }
}

struct PointInputField: View {
  var name = ""
  @State var points = 0
  @FocusState private var inputFocused: Bool

  var body: some View {
    TextField(
      name,
      value: $points,
      format: IntegerFormatStyle()
    )
    .focused($inputFocused)
    .textInputAutocapitalization(.never)
    .disableAutocorrection(true)
    .multilineTextAlignment(.trailing)
    .textFieldStyle(.roundedBorder)
    .border(.secondary)
    .submitLabel(.done)
    Button {
      //
    } label: {
      Image(systemName: "checkmark")
    }
    Button {
      //
    } label: {
      Image(systemName: "arrow.uturn.backward")
    }
  }
}

#Preview {
  ActivePlayerListItem()
}

#Preview {
  PointInputField(
    name: "Test"
  )
}
