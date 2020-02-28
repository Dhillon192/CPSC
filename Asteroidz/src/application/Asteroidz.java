package application;

import javafx.animation.AnimationTimer;

import javafx.application.Application;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;



import java.util.List;

import java.util.stream.Collectors;

public class Asteroidz extends Application {

    private Pane root = new Pane();
    private double t = 0;
    private Body player = new Body(200, 500, 40, 40, "player", Color.BLUE);

    public Parent createContent() {
    	
        root.setPrefSize(600, 600);
        root.getChildren().add(player);
        AnimationTimer timer = new AnimationTimer() {

            public void handle(long now) {
                update();
            }
        };
        timer.start();
        nextLevel();
        return root;
    }
    public void nextLevel() {
    	
    	//creates asteroids (need to change)
        for (int i = 0; i < 5; i++) {
            Body b = new Body(90 + i*100, 150, 30, 30, "enemy", Color.BROWN);
            root.getChildren().add(b);
        }
    }

    public List<Body> bodies() {
        return root.getChildren().stream().map(n -> (Body)n).collect(Collectors.toList());
    }

    public void update() {
        t += 0.016;
        bodies().forEach(b -> {

            switch (b.type) {
                case "playerbullet": //need to change to be able to shoot in all directions
                    b.moveUp();
                    bodies().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {

                        if (b.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.dead = true;
                            b.dead = true;
                        }
                    });
                    break;
            }
        });

        root.getChildren().removeIf(n -> {
            Body b = (Body) n;
            return b.dead;
        });
    }
    
    //creates bullet that shoots from player position
    public void shoot(Body who) {
        Body b = new Body((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.type + "bullet", Color.BLACK);
        root.getChildren().add(b);
    }

    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {

                case A:
                    player.moveLeft();
                    break;

                case D:
                    player.moveRight();
                    break;

                case W:
                    player.moveUp();
                    break;
                
                case S:
                    player.moveDown();
                    break;
                    
                case SPACE:
                    shoot(player);
                    break;       
            }
        });
        stage.setScene(scene);
        stage.show();
    }
    public static class Body extends Rectangle {
        private boolean dead = false;
        private final String type;

        Body(int x, int y, int w, int h, String type, Color color) {
            super(w, h, color);
            this.type = type;
            setTranslateX(x);
            setTranslateY(y);

        }
        void moveLeft() {
            setTranslateX(getTranslateX() - 5);
        }
        void moveRight() {
            setTranslateX(getTranslateX() + 5);
        }
        void moveUp() {
            setTranslateY(getTranslateY() - 5);
        }
        void moveDown() {
            setTranslateY(getTranslateY() + 5);
        }
    }

    public static void main(String[] args) {
        launch(args);

    }

}