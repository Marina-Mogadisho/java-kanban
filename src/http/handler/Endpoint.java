package http.handler;

public class Endpoint {
    private final EndpointType type;
    private Integer id;

    public enum EndpointType {
        GET, GET_ID, POST_CREATE, POST_UPDATE, DELETE_ID, GET_SUB_ID, UNKNOWN
    }

    public Endpoint(String handler, String requestPath, String requestMethod) {
        id = null;

        String[] pathParts = requestPath.split("/");
        if (pathParts.length > 1) {
            if (!pathParts[1].equalsIgnoreCase(handler)) {
                type = EndpointType.UNKNOWN;
                return;
            }
        }
        //--------------------------------------------------------------
        if (pathParts.length == 2) {
            if (requestMethod.equals("GET")) {
                type = EndpointType.GET;
                return;
            }
            if (requestMethod.equals("POST")) {
                type = EndpointType.POST_CREATE;
                return;
            }
        }
        //--------------------------------------------------------------
        if (pathParts.length == 3) {
            if (requestMethod.equals("GET")) {
                type = EndpointType.GET_ID;
                setId(pathParts);
                return;
            }

            if (requestMethod.equals("POST")) {
                type = EndpointType.POST_UPDATE;
                setId(pathParts);
                return;
            }
            if (requestMethod.equals("DELETE")) {
                type = EndpointType.DELETE_ID;
                setId(pathParts);
                return;
            }
        }
        //--------------------------------------------------------------
        if (pathParts.length == 4) {
            if (requestMethod.equals("GET") && "subtasks".equalsIgnoreCase(pathParts[3])) {
                type = EndpointType.GET_SUB_ID;
                setId(pathParts);
                return;
            }
        }
        type = EndpointType.UNKNOWN;
    }

    public EndpointType getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    private void setId(String[] pathParts) {
        id = null;
        if (pathParts.length > 2)
            try {
                id = Integer.parseInt(pathParts[2]);
            } catch (Exception e) {
                id = null;
            }
    }
}
