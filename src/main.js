import { SpaceModule } from "./spaceModule.js";
import { ItemBindingStore } from "./itemBinding.js";
import { renderArViewer } from "./arViewer.js";

const generateSpaceBtn = document.querySelector("#generate-space-btn");
const launchArBtn = document.querySelector("#launch-ar-btn");
const spaceNodeList = document.querySelector("#space-node-list");
const bindingForm = document.querySelector("#binding-form");
const bindingList = document.querySelector("#binding-list");
const nodeSelect = document.querySelector("#node-select");
const arView = document.querySelector("#ar-view");
const arStatus = document.querySelector("#ar-status");

const spaceModule = new SpaceModule();
const itemBindingStore = new ItemBindingStore();

function renderSpaceNodes() {
  spaceNodeList.innerHTML = "";
  nodeSelect.innerHTML = "";

  for (const node of spaceModule.nodes) {
    const card = document.createElement("article");
    card.className = "node-card";
    card.innerHTML = `
      <h3>${node.label}</h3>
      <p>ID: ${node.id}</p>
      <p>Position: (${node.position.x}, ${node.position.y}, ${node.position.z})</p>
      <p>Dim: ${node.dimensions.w} × ${node.dimensions.h} × ${node.dimensions.d}</p>
      <p>Capacity: ${node.capacity}</p>
    `;
    spaceNodeList.append(card);

    const option = document.createElement("option");
    option.value = node.id;
    option.textContent = `${node.label} (${node.id})`;
    nodeSelect.append(option);
  }
}

function renderBindings() {
  bindingList.innerHTML = "";
  for (const binding of itemBindingStore.listAll()) {
    const li = document.createElement("li");
    li.textContent = `${binding.itemName} x${binding.quantity} → ${binding.nodeLabel}`;
    bindingList.append(li);
  }
}

generateSpaceBtn?.addEventListener("click", () => {
  spaceModule.refreshMockNodes();
  renderSpaceNodes();
  arStatus.textContent = "Space nodes refreshed. Relaunch AR Viewer to refresh preview.";
});

bindingForm?.addEventListener("submit", (event) => {
  event.preventDefault();
  const nameInput = document.querySelector("#item-name");
  const qtyInput = document.querySelector("#item-qty");

  const itemName = nameInput.value.trim();
  const quantity = Number(qtyInput.value);
  const node = spaceModule.getNode(nodeSelect.value);

  if (!itemName || !quantity || !node) {
    return;
  }

  itemBindingStore.bindItem({ itemName, quantity, node });
  renderBindings();
  bindingForm.reset();
});

launchArBtn?.addEventListener("click", async () => {
  await renderArViewer(
    {
      nodes: spaceModule.nodes,
      bindings: itemBindingStore.listAll(),
    },
    {
      view: arView,
      status: arStatus,
    },
  );
});

spaceModule.refreshMockNodes();
renderSpaceNodes();
