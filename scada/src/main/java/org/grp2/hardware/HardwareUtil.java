package org.grp2.hardware;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class HardwareUtil {
    /**
     * Converts a CubeNodeId to a NodeId
     *
     * @param cubeNodeId any CubeNodeId
     * @return a node id
     */
    public static NodeId getNodeId(CubeNodeId cubeNodeId) {
        return new NodeId(6, cubeNodeId.getNodeId());
    }
}