package com.smartstoragear.navigation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.ar.core.ArCoreApk
import com.smartstoragear.feature.capture.CameraCaptureView
import com.smartstoragear.feature.capture.CaptureViewModel
import com.smartstoragear.feature.home.HomeViewModel
import com.smartstoragear.feature.item.AddItemViewModel
import com.smartstoragear.feature.item.ItemDetailViewModel
import com.smartstoragear.feature.search.SearchViewModel
import com.smartstoragear.feature.settings.SettingsScreen
import com.smartstoragear.feature.space.CreateSpaceViewModel
import com.smartstoragear.feature.space.ReviewViewModel
import com.smartstoragear.feature.space.SpaceDetailViewModel

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {
            val vm: HomeViewModel = hiltViewModel()
            val spaces by vm.spaces.collectAsState()
            HomeScreen(spaces.map { it.id to it.name },
                onCreate = { nav.navigate(NavRoutes.CREATE_SPACE) },
                onOpenSpace = { nav.navigate("space/$it") },
                onSearch = { nav.navigate(NavRoutes.SEARCH) },
                onSettings = { nav.navigate(NavRoutes.SETTINGS) })
        }
        composable(NavRoutes.CREATE_SPACE) {
            val vm: CreateSpaceViewModel = hiltViewModel()
            CreateSpaceScreen(onDone = { name -> vm.createSpace(name) { id -> nav.navigate("capture/$id") } })
        }
        composable(NavRoutes.CAPTURE, arguments = listOf(navArgument("spaceId") { type = NavType.StringType })) {
            val vm: CaptureViewModel = hiltViewModel()
            val photos by vm.photos.collectAsState()
            val error by vm.error.collectAsState()
            CaptureScreen(
                count = photos.size,
                error = error,
                onCapture = vm::onPhotoCaptured,
                onReview = {
                    val id = it.arguments?.getString("spaceId")
                    if (vm.canProceed()) nav.navigate("review/$id")
                }
            )
        }
        composable(NavRoutes.REVIEW, arguments = listOf(navArgument("spaceId") { type = NavType.StringType })) {
            val captureVm: CaptureViewModel = hiltViewModel(it)
            val reviewVm: ReviewViewModel = hiltViewModel(it)
            val photos by captureVm.photos.collectAsState()
            ReviewScreen(count = photos.size, onFinalize = {
                reviewVm.finalizeScan(photos) { nav.navigate("space/${reviewVm.spaceId}") }
            })
        }
        composable(NavRoutes.SPACE_DETAIL, arguments = listOf(navArgument("spaceId") { type = NavType.StringType })) {
            val vm: SpaceDetailViewModel = hiltViewModel()
            val space by vm.space.collectAsState()
            val nodes by vm.nodes.collectAsState()
            SpaceDetailScreen(space?.name ?: "Space", nodes.map { it.id to it.name },
                onAddItem = { nav.navigate("add_item/${it.arguments?.getString("spaceId")}") },
                onAr = { nav.navigate("ar/${it.arguments?.getString("spaceId")}") })
        }
        composable(NavRoutes.ADD_ITEM, arguments = listOf(navArgument("spaceId") { type = NavType.StringType })) {
            val vm: AddItemViewModel = hiltViewModel()
            val nodes by vm.nodes.collectAsState()
            AddItemScreen(nodes = nodes.map { it.id to it.name }, onDone = { n, c, note, t, node -> vm.addItem(n, c, note, t, node) { nav.popBackStack() } })
        }
        composable(NavRoutes.ITEM_DETAIL, arguments = listOf(navArgument("itemId") { type = NavType.StringType })) {
            val vm: ItemDetailViewModel = hiltViewModel()
            val item by vm.item.collectAsState()
            ItemDetailScreen(item?.name ?: "-", item?.tags?.joinToString() ?: "")
        }
        composable(NavRoutes.SEARCH) {
            val vm: SearchViewModel = hiltViewModel()
            val q by vm.query.collectAsState()
            val result by vm.results.collectAsState()
            SearchScreen(q, result.map { it.id to it.name }, onQuery = { vm.query.value = it }, onOpenItem = { nav.navigate("item/$it") })
        }
        composable(NavRoutes.AR_VIEWER, arguments = listOf(navArgument("spaceId") { type = NavType.StringType })) {
            ARViewerScreen()
        }
        composable(NavRoutes.SETTINGS) { SettingsScreen() }
    }
}

@Composable
fun HomeScreen(spaces: List<Pair<Long, String>>, onCreate: () -> Unit, onOpenSpace: (Long) -> Unit, onSearch: () -> Unit, onSettings: () -> Unit) {
    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Smart Storage AR")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onCreate) { Text("Create Space") }
                Button(onClick = onSearch) { Text("Search") }
                Button(onClick = onSettings) { Text("Settings") }
            }
            LazyColumn {
                items(spaces) { space ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onOpenSpace(space.first) }) {
                        Text(space.second, Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateSpaceScreen(onDone: (String) -> Unit) {
    val name = remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create Storage Space")
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Space name") })
        Button(onClick = { onDone(name.value) }, enabled = name.value.isNotBlank()) { Text("Start Capture") }
    }
}

@Composable
fun CaptureScreen(count: Int, error: String?, onCapture: (String, android.graphics.Bitmap) -> Unit, onReview: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Capture photos: $count / 7 (minimum 3)", Modifier.padding(8.dp))
        error?.let { Text(it, Modifier.padding(8.dp)) }
        androidx.compose.foundation.layout.Box(Modifier.weight(1f)) { CameraCaptureView(onCaptured = onCapture) }
        Button(onClick = onReview, enabled = count >= 3, modifier = Modifier.fillMaxWidth().padding(8.dp)) { Text("Review") }
    }
}

@Composable
fun ReviewScreen(count: Int, onFinalize: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Review $count photos")
        Text("Cloud modeling unavailable: mock node generation will be used.")
        Button(onClick = onFinalize) { Text("Finalize Scan") }
    }
}

@Composable
fun SpaceDetailScreen(name: String, nodes: List<Pair<Long, String>>, onAddItem: () -> Unit, onAr: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Space: $name")
        Button(onClick = onAddItem) { Text("Add Item") }
        Button(onClick = onAr) { Text("Open AR") }
        Text("Nodes")
        nodes.forEach { Text("• ${it.second}") }
    }
}

@Composable
fun AddItemScreen(nodes: List<Pair<Long, String>>, onDone: (String, String, String, List<String>, Long?) -> Unit) {
    val name = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val note = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }
    val node = remember { mutableStateOf<Long?>(nodes.firstOrNull()?.first) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(name.value, { name.value = it }, label = { Text("Name") })
        OutlinedTextField(category.value, { category.value = it }, label = { Text("Category") })
        OutlinedTextField(note.value, { note.value = it }, label = { Text("Note") })
        OutlinedTextField(tags.value, { tags.value = it }, label = { Text("Tags comma-separated") })
        nodes.forEach { n -> Text(n.second, Modifier.clickable { node.value = n.first }) }
        Button(onClick = { onDone(name.value, category.value, note.value, tags.value.split(","), node.value) }, enabled = name.value.isNotBlank()) { Text("Save Item") }
    }
}

@Composable
fun ItemDetailScreen(name: String, tags: String) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Item: $name")
        Text("Tags: $tags")
    }
}

@Composable
fun SearchScreen(query: String, items: List<Pair<Long, String>>, onQuery: (String) -> Unit, onOpenItem: (Long) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = query, onValueChange = onQuery, label = { Text("Search by name or tag") })
        LazyColumn {
            items(items) { item ->
                Text(item.second, Modifier.fillMaxWidth().clickable { onOpenItem(item.first) }.padding(8.dp))
            }
        }
    }
}

@Composable
fun ARViewerScreen() {
    val context = LocalContext.current
    val availability = ArCoreApk.getInstance().checkAvailability(context)
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (availability.isSupported) {
            Text("ARCore supported. Showing label overlay preview.")
            Text("[Node] Cabinet A @ (-1.0, 0.0, -2.0)")
            Text("[Node] Shelf Top @ (1.0, 1.2, -1.5)")
        } else {
            Text("AR unavailable on this device. Fallback to list view.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(spaces = listOf(1L to "Garage"), onCreate = {}, onOpenSpace = {}, onSearch = {}, onSettings = {})
}
