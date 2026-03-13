/**
 * @typedef {Object} ItemBinding
 * @property {string} id
 * @property {string} itemName
 * @property {number} quantity
 * @property {string} nodeId
 * @property {string} nodeLabel
 */

export class ItemBindingStore {
  constructor() {
    /** @type {ItemBinding[]} */
    this.bindings = [];
  }

  /**
   * @param {{itemName: string, quantity:number, node:{id:string,label:string}}} payload
   * @returns {ItemBinding}
   */
  bindItem(payload) {
    const binding = {
      id: `IT-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`,
      itemName: payload.itemName.trim(),
      quantity: payload.quantity,
      nodeId: payload.node.id,
      nodeLabel: payload.node.label,
    };

    this.bindings.push(binding);
    return binding;
  }

  listByNode(nodeId) {
    return this.bindings.filter((binding) => binding.nodeId === nodeId);
  }

  listAll() {
    return [...this.bindings];
  }
}
