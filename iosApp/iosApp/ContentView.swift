import SwiftUI
//import shared

struct ContentView: View {
//	let greet = Greeting().greet()

	var body: some View {
    NavigationStack{
      Form {
        Section{
          Text("Testing")
          Text("More test")
        }
        Section{
          Text("New section")
        }
      }
      .navigationTitle("Nav test")
      .navigationBarTitleDisplayMode(.inline)
    }
  }
}

#Preview {
  ContentView()
}
//struct ContentView_Previews: PreviewProvider {
//	static var previews: some View {
//		ContentView()
//	}
//}
