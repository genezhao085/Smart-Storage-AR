package com.smartstoragear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

data class StorageNode(
    val id: Int,
    val name: String,
    val locationHint: String
)

data class StoredItem(
    val id: Int,
    val nodeId: Int,
    val label: String
)

class FakeStorageRepository {
    private var nextNodeId = 3
    private var nextItemId = 4

    private val nodes = mutableListOf(
        StorageNode(1, "Shelf A", "Near garage door"),
        StorageNode(2, "Top Cabinet", "Kitchen corner")
    )

    private val items = mutableListOf(
        StoredItem(1, 1, "Power Drill"),
        StoredItem(2, 1, "Extension Cable"),
        StoredItem(3, 2, "Tea Box")
    )

    fun allNodes(): List<StorageNode> = nodes.toList()

    fun addNode(name: String, locationHint: String): StorageNode {
        val node = StorageNode(nextNodeId++, name, locationHint)
        nodes += node
        return node
    }

    fun itemsForNode(nodeId: Int): List<StoredItem> = items.filter { it.nodeId == nodeId }

    fun addItem(nodeId: Int, label: String): StoredItem {
        val item = StoredItem(nextItemId++, nodeId, label)
        items += item
        return item
    }

    // TODO: Replace fake in-memory repository with cloud-backed storage and sync model.
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Smart Storage AR MVP") {
        val repository = remember { FakeStorageRepository() }
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                StorageApp(repository)
            }
        }
    }
}

@Composable
private fun StorageApp(repository: FakeStorageRepository) {
    var nodes by remember { mutableStateOf(repository.allNodes()) }
    var selectedNodeId by remember { mutableStateOf(nodes.firstOrNull()?.id) }
    var visibleItems by remember(selectedNodeId) {
        mutableStateOf(selectedNodeId?.let { repository.itemsForNode(it) }.orEmpty())
    }

    var newNodeName by remember { mutableStateOf("") }
    var newNodeHint by remember { mutableStateOf("") }
    var newItemName by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Storage Nodes", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = nodes, key = { it.id }) { node ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(node.name, style = MaterialTheme.typography.titleMedium)
                            Text(node.locationHint, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                            Button(onClick = {
                                selectedNodeId = node.id
                                visibleItems = repository.itemsForNode(node.id)
                            }) {
                                Text(if (selectedNodeId == node.id) "Selected" else "Select")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = newNodeName,
                onValueChange = { newNodeName = it },
                label = { Text("New node name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = newNodeHint,
                onValueChange = { newNodeHint = it },
                label = { Text("Location hint") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (newNodeName.isBlank() || newNodeHint.isBlank()) {
                        return@Button
                    }
                    val newNode = repository.addNode(newNodeName.trim(), newNodeHint.trim())
                    nodes = repository.allNodes()
                    selectedNodeId = newNode.id
                    visibleItems = repository.itemsForNode(newNode.id)
                    newNodeName = ""
                    newNodeHint = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Node")
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            val selectedNode = nodes.firstOrNull { it.id == selectedNodeId }
            Text("Items + AR Preview", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Selected: ${selectedNode?.name ?: "None"}")
            Spacer(Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("AR Overlay (Mock)", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "This MVP shows a simulated overlay for the chosen storage node.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    // TODO: Connect to real AR cloud anchors and 3D model placement pipeline.
                }
            }

            Spacer(Modifier.height(12.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(items = visibleItems, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(item.label, modifier = Modifier.padding(12.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = newItemName,
                onValueChange = { newItemName = it },
                label = { Text("New item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val nodeId = selectedNodeId
                    if (nodeId == null || newItemName.isBlank()) {
                        return@Button
                    }
                    repository.addItem(nodeId, newItemName.trim())
                    visibleItems = repository.itemsForNode(nodeId)
                    newItemName = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Item")
            }
        }
    }
}
