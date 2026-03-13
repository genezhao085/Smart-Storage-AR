const DEFAULT_NODE_COUNT = 6;

/**
 * @typedef {Object} SpaceNode
 * @property {string} id
 * @property {string} label
 * @property {{x:number,y:number,z:number}} position
 * @property {{w:number,h:number,d:number}} dimensions
 * @property {number} capacity
 */

/**
 * Generate deterministic mock space nodes.
 * @param {number} count
 * @returns {SpaceNode[]}
 */
export function generateMockSpaceNodes(count = DEFAULT_NODE_COUNT) {
  return Array.from({ length: count }, (_, i) => {
    const idx = i + 1;
    return {
      id: `SN-${String(idx).padStart(3, "0")}`,
      label: `Shelf ${idx}`,
      position: {
        x: (idx % 3) * 0.9,
        y: idx > 3 ? 1.1 : 0.2,
        z: Math.floor((idx - 1) / 3) * 0.8,
      },
      dimensions: {
        w: 0.8,
        h: idx > 3 ? 0.5 : 0.6,
        d: 0.5,
      },
      capacity: 10 + idx * 3,
    };
  });
}

export class SpaceModule {
  constructor() {
    /** @type {SpaceNode[]} */
    this.nodes = [];
  }

  refreshMockNodes(count = DEFAULT_NODE_COUNT) {
    this.nodes = generateMockSpaceNodes(count);
    return this.nodes;
  }

  getNode(id) {
    return this.nodes.find((node) => node.id === id) ?? null;
  }
}
