function supportsNativeAR() {
  return "xr" in navigator;
}

function supportsDeviceOrientation() {
  return "DeviceOrientationEvent" in window;
}

/**
 * Render AR preview with graceful fallback.
 * @param {{nodes: any[], bindings: any[]}} model
 * @param {{view: HTMLElement, status: HTMLElement}} targets
 */
export async function renderArViewer(model, targets) {
  const { view, status } = targets;
  view.innerHTML = "";

  if (supportsNativeAR()) {
    status.textContent = "WebXR detected. Running simulated AR overlay in-browser.";
    view.append(renderOverlay(model, "webxr"));
    return;
  }

  if (supportsDeviceOrientation()) {
    status.textContent =
      "WebXR unavailable. Using device-orientation fallback overlay for AR-like preview.";
    view.append(renderOverlay(model, "orientation"));
    return;
  }

  status.textContent =
    "AR capabilities unavailable. Showing 2D fallback layout with node/item mapping.";
  view.append(renderStaticFallback(model));
}

function renderOverlay(model, mode) {
  const card = document.createElement("div");
  card.className = "overlay-card";

  const title = document.createElement("h3");
  title.textContent = `AR Overlay (${mode})`;
  card.append(title);

  const summary = document.createElement("p");
  summary.textContent = `${model.nodes.length} nodes • ${model.bindings.length} bound item groups`;
  card.append(summary);

  const list = document.createElement("ul");
  list.className = "fallback-list";

  for (const node of model.nodes) {
    const count = model.bindings
      .filter((binding) => binding.nodeId === node.id)
      .reduce((total, binding) => total + binding.quantity, 0);

    const li = document.createElement("li");
    li.textContent = `${node.label} (${node.id}) → ${count} items`;
    list.append(li);
  }

  card.append(list);
  return card;
}

function renderStaticFallback(model) {
  const wrapper = document.createElement("div");
  wrapper.className = "overlay-card";

  const title = document.createElement("h3");
  title.textContent = "2D Fallback Layout";
  wrapper.append(title);

  const table = document.createElement("table");
  table.className = "fallback-table";
  table.innerHTML = `
    <thead>
      <tr><th>SpaceNode</th><th>Position</th><th>Items</th></tr>
    </thead>
    <tbody></tbody>
  `;

  const body = table.querySelector("tbody");

  for (const node of model.nodes) {
    const items = model.bindings
      .filter((binding) => binding.nodeId === node.id)
      .map((binding) => `${binding.itemName} x${binding.quantity}`)
      .join(", ") || "—";

    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${node.label}</td>
      <td>${node.position.x.toFixed(1)}, ${node.position.y.toFixed(1)}, ${node.position.z.toFixed(1)}</td>
      <td>${items}</td>
    `;
    body?.append(row);
  }

  wrapper.append(table);
  return wrapper;
}
