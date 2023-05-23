public class Camera {
    private float cameraX, cameraY, zoomLevel;

    public Camera(float cameraX, float cameraY, float zoomLevel) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.zoomLevel = zoomLevel;
    }

    public float getCameraX() {
        return cameraX;
    }

    public void setCameraX(float cameraX) {
        this.cameraX = cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
