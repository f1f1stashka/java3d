import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        Canvas mainCanvas = new Canvas(1280, 720);
        Camera mainCamera = new Camera(Vertex3.zero(), Vertex3.zero());
        double camSpeed = 0.1;
        double camRotSpeed = 0.1;
        mainCanvas.init();
        mainCanvas.canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W:
                        mainCamera.move(new Vertex3(0, 0, camSpeed));
                        break;
                    case KeyEvent.VK_A:
                        mainCamera.move(new Vertex3(-camSpeed, 0, 0));
                        break;
                    case KeyEvent.VK_S:
                        mainCamera.move(new Vertex3(0, 0, -camSpeed));
                        break;
                    case KeyEvent.VK_D:
                        mainCamera.move(new Vertex3(camSpeed, 0, 0));
                        break;
                    case KeyEvent.VK_SPACE:
                        mainCamera.move(new Vertex3(0, -camSpeed, 0));
                        break;
                    case KeyEvent.VK_SHIFT:
                        mainCamera.move(new Vertex3(0, camSpeed, 0));
                        break;
                    case KeyEvent.VK_LEFT:
                        mainCamera.rotate(new Vertex3(camRotSpeed, 0, 0));
                        break;
                    case KeyEvent.VK_RIGHT:
                        mainCamera.rotate(new Vertex3(-camRotSpeed, 0, 0));
                        break;
                    case KeyEvent.VK_UP:
                        mainCamera.rotate(new Vertex3(0, camRotSpeed, 0));
                        break;
                    case KeyEvent.VK_DOWN:
                        mainCamera.rotate(new Vertex3(0, -camRotSpeed, 0));
                        break;
                    case KeyEvent.VK_L:
                        mainCamera.rotate(new Vertex3(0, 0, camRotSpeed));
                        break;
                    case KeyEvent.VK_K:
                        mainCamera.rotate(new Vertex3(0, 0, -camRotSpeed));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        while(true){
            clearCanvas(mainCanvas);
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    drawCube(mainCanvas, new Vertex3(i*100, 0, j*100), mainCamera);
                }
            }
            mainCanvas.renderImage();
        }

    }

    public static double clamp(double min, double max, double var){
        return Math.max(min, Math.min(max, var));
    }

    public static void clearCanvas(Canvas canvas){
        canvas.bufferedImageGraphics.setColor(Color.white);
        canvas.bufferedImageGraphics.fillRect(0, 0, canvas.width, canvas.height);
    }
    public static boolean isLinesInter(Vertex2 p1, Vertex2 p2, Vertex2 p3, Vertex2 p4){
        if(p2.x < p1.x){
            Vertex2 temp = p1;
            p1 = p2;
            p2 = temp;
        }
        if(p4.x < p3.x){
            Vertex2 temp = p3;
            p3 = p4;
            p4 = temp;
        }
        if(p2.x < p3.x){
            return false;
        }
        if(p1.x - p2.x == 0 && p3.x - p4.x == 0){
            if(p1.x == p3.x){
                if(!((Math.max(p1.y, p2.y) < Math.min(p3.y, p4.y)) ||
                        (Math.min(p1.y, p2.y) > Math.max(p3.y, p4.y)))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static Vertex2 getLinesInterPos(Vertex2 p1, Vertex2 p2, Vertex2 p3, Vertex2 p4){
        Vertex2 interPos = new Vertex2(0, 0);
        if(p2.x < p1.x){
            Vertex2 temp = p1;
            p1 = p2;
            p2 = temp;
        }
        if(p4.x < p3.x){
            Vertex2 temp = p3;
            p3 = p4;
            p4 = temp;
        }
        double a1 = (p1.y-p2.y)/(p1.x-p2.x);
        double a2 = (p3.y-p4.y)/(p3.x-p4.x);
        double b1 = p1.y-(a1*p1.x);
        double b2 = p3.y-(a2*p3.x);
        interPos.x = (b2-b1)/(a1-a2);
        interPos.y = a2 * interPos.x + b2;
        return interPos;
    }

    public static void drawCube(Canvas canvas, Vertex3 pos, Camera camera){

        Vertex3[] apex = {
                new Vertex3(pos.x, pos.y, pos.z),
                new Vertex3(pos.x+100, pos.y, pos.z),
                new Vertex3(pos.x, pos.y+100, pos.z),
                new Vertex3(pos.x, pos.y, pos.z+100),
                new Vertex3(pos.x+100, pos.y+100, pos.z),
                new Vertex3(pos.x, pos.y+100, pos.z+100),
                new Vertex3(pos.x+100, pos.y, pos.z+100),
                new Vertex3(pos.x+100, pos.y+100, pos.z+100)
        };
        double focus = 0.1;

        Vertex2 displayPos[] = {
                getDisplayPosition(apex[0], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[1], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[2], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[3], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[4], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[5], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[6], camera.position, camera.rotation, focus, 70, 70),
                getDisplayPosition(apex[7], camera.position, camera.rotation, focus, 70, 70)
        };

        for(int i = 0; i < 12; i++){
            if(isLinesInter(displayPos[i], displayPos[i+1], 0, 1280));
        }

        int xx[] = {(int) displayPos[0].x, (int) displayPos[1].x, (int) displayPos[2].x, (int) displayPos[3].x,
                (int) displayPos[4].x, (int) displayPos[5].x, (int) displayPos[6].x, (int) displayPos[7].x};

        int yy[] = {(int) displayPos[0].y, (int) displayPos[1].y, (int) displayPos[2].y, (int) displayPos[3].y,
                (int) displayPos[4].y, (int) displayPos[5].y, (int) displayPos[6].y, (int) displayPos[7].y};



        canvas.bufferedImageGraphics.setColor(Color.red);
        for(int i = 0; i < 8; i++){
            canvas.bufferedImageGraphics.fillOval(xx[i]-2, yy[i]-2, 4, 4);
        }

        int xx1[] = {xx[0], xx[1], xx[6], xx[3]};
        int yy1[] = {yy[0], yy[1], yy[6], yy[3]};
        System.out.println(xx[1] + " " + yy[1]);

        int xx2[] = {xx[0], xx[3], xx[5], xx[2]};
        int yy2[] = {yy[0], yy[3], yy[5], yy[2]};

        int xx3[] = {xx[3], xx[5], xx[7], xx[6]};
        int yy3[] = {yy[3], yy[5], yy[7], yy[6]};

        int xx4[] = {xx[2], xx[5], xx[7], xx[4]};
        int yy4[] = {yy[2], yy[5], yy[7], yy[4]};

        int xx5[] = {xx[4], xx[7], xx[6], xx[1]};
        int yy5[] = {yy[4], yy[7], yy[6], yy[1]};

        int xx6[] = {xx[0], xx[1], xx[4], xx[2]};
        int yy6[] = {yy[0], yy[1], yy[4], yy[2]};

        canvas.bufferedImageGraphics.setColor(Color.black);

        canvas.bufferedImageGraphics.drawPolygon(xx1, yy1, 4);
        canvas.bufferedImageGraphics.drawPolygon(xx2, yy2, 4);
        canvas.bufferedImageGraphics.drawPolygon(xx3, yy3, 4);
        canvas.bufferedImageGraphics.drawPolygon(xx4, yy4, 4);
        canvas.bufferedImageGraphics.drawPolygon(xx5, yy5, 4);
        canvas.bufferedImageGraphics.drawPolygon(xx6, yy6, 4);
    }

    public static Vertex2 getDisplayPosition(Vertex3 dotPosition, Vertex3 cameraPosition, Vertex3 cameraRotation, double focus, double alpha, double beta){
        Vertex3 relativePos = Vertex3.getDif(dotPosition, cameraPosition);

        Vertex3 radRotation = new Vertex3(Math.toRadians(cameraRotation.x),
                Math.toRadians(cameraRotation.y), Math.toRadians(cameraRotation.z));

        Vertex3 rotatedPos = new Vertex3(relativePos.x,
                relativePos.y * Math.cos(radRotation.x) + relativePos.z * Math.sin(radRotation.x),
                -relativePos.y * Math.sin(radRotation.x) + relativePos.z * Math.cos(radRotation.x));

        rotatedPos = new Vertex3(rotatedPos.x * Math.cos(radRotation.y) + rotatedPos.z * Math.sin(radRotation.y),
                rotatedPos.y,
                -rotatedPos.x * Math.sin(radRotation.y) + rotatedPos.z * Math.cos(radRotation.y));

        rotatedPos = new Vertex3(rotatedPos.x * Math.cos(radRotation.z) - rotatedPos.y * Math.sin(radRotation.z),
                rotatedPos.x * Math.sin(radRotation.z) + rotatedPos.y * Math.cos(radRotation.z),
                rotatedPos.z);

        double alphaRad = Math.toRadians(alpha);
        double betaRad = Math.toRadians(beta);

        double distance = getDistance(cameraPosition, dotPosition);

        Vertex3 planePos = new Vertex3(0, 0, 10);

        //Vertex2 finalPos = new Vertex2(rotatedPos.x * (focus / distance) + 640,
        //        rotatedPos.y * (focus / distance) + 360);


        Vertex2 finalPos = new Vertex2((planePos.z / rotatedPos.z) * rotatedPos.x + planePos.x + 640,
                (planePos.z / rotatedPos.z) * rotatedPos.y + planePos.y + 360);

        return finalPos;
    }

    public static double getDistance(Vertex3 vertex1, Vertex3 vertex2){
        return Math.sqrt(Math.pow(vertex1.x - vertex2.x, 2) + Math.pow(vertex1.y - vertex2.y, 2) + Math.pow(vertex1.z - vertex2.z, 2));
    }
}

class Vertex3{
    double x;
    double y;
    double z;
    Vertex3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vertex3 getSum(Vertex3 ... vertexes){
        Vertex3 vertexSum = new Vertex3(0, 0, 0);
        for(int i = 0; i < vertexes.length; i++){
            vertexSum.x += vertexes[i].x;
            vertexSum.y += vertexes[i].y;
            vertexSum.z += vertexes[i].z;
        }
        return vertexSum;
    }
    public static Vertex3 getDif(Vertex3 vertex1, Vertex3 vertex2){
        return new Vertex3(vertex1.x - vertex2.x, vertex1.y - vertex2.y, vertex1.z - vertex2.z);
    }

    public static Vertex3 zero(){
        return new Vertex3(0, 0, 0);
    }
}

class Vertex2{
    double x;
    double y;
    Vertex2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Vertex2 getSum(Vertex2 ... vertexes){
        Vertex2 vertexSum = new Vertex2(0, 0);
        for(int i = 0; i < vertexes.length; i++){
            vertexSum.x += vertexes[i].x;
            vertexSum.y += vertexes[i].y;
        }
        return vertexSum;
    }

    public static Vertex2 getDif(Vertex2 vertex1, Vertex2 vertex2){
        return new Vertex2(vertex1.x - vertex2.x, vertex1.y - vertex2.y);
    }
}

class Canvas{
    int width;
    int height;
    JFrame canvas;
    Graphics canvasGraphics;
    BufferedImage bufferedImage;
    Graphics bufferedImageGraphics;
    Canvas(int width, int height){
        this.width = width;
        this.height = height;
        this.canvas = new JFrame();
        this.bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    }
    public void init(){
        this.canvas.setSize(this.width, this.height);
        this.bufferedImageGraphics = this.bufferedImage.getGraphics();
        this.canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.canvas.setLocationRelativeTo(null);
        this.canvas.setVisible(true);
        this.canvasGraphics = this.canvas.getGraphics();
    }
    public void renderImage(){
        this.canvasGraphics.drawImage(this.bufferedImage, 0, 0, null);
    }
}

class Camera{
    Vertex3 position;
    Vertex3 rotation;
    Camera(Vertex3 position, Vertex3 rotation){
        this.position = position;
        this.rotation = rotation;
    }

    public void move(Vertex3 moveVertex){
        this.position = Vertex3.getSum(this.position, moveVertex);
    }

    public void rotate(Vertex3 rotateVertex){
        this.rotation = Vertex3.getSum(this.rotation, rotateVertex);
    }
}