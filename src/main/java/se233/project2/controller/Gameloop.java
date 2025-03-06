package se233.project2.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.project2.view.Platform;
import se233.project2.model.Boss;
import se233.project2.model.Bullet;
import se233.project2.model.Character;
import se233.project2.model.Enemy;

import java.util.*;

public class Gameloop implements Runnable{
    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;
    private GameOver gameOver;
    private boolean allEnemiesRemoved = false;
    private boolean hasEnemy = true;
    private boolean bossAlive = true;
    private boolean bossDefeated = false;
    Logger logger = LoggerFactory.getLogger(Gameloop.class);

    public Gameloop(Platform platform){
        this.platform = platform;
        frameRate = 30;
        interval = 1000.0f / frameRate;
        running = true;
        gameOver = new GameOver();
        gameOver.setVisible(false);
        platform.getChildren().add(gameOver);
    }

    public void update(se233.project2.model.Character character){
        if(platform.getKey().ispressed(character.getLeftkey())){
            character.setScaleX(-1);
            character.moveLeft();
            platform.getCharacter().trace();
        }
        if(platform.getKey().ispressed(character.getRightkey())){
            character.setScaleX(+1);
            character.moveRight();
            platform.getCharacter().trace();
        }
        if(platform.getKey().ispressed(character.getLeftkey()) || platform.getKey().ispressed(character.getRightkey())){
            character.getImageView().tick();
        }
        if (!platform.getKey().ispressed(character.getLeftkey()) && !platform.getKey().ispressed(character.getRightkey())) {
            character.stopMoving();
        }
        //if (character.getBoundsInParent().intersects(platform.getEnemy().getBoundsInParent()));
        if (platform.getKey().ispressed(character.getShootekey())) {
            character.shoot();
        }
        if (platform.getKey().ispressed(character.getBshoot())) {
            character.shootSpecial();
        }
    }
    private void moveBullets() {
        Platform platform = (Platform) this.platform;
        List<Node> bullets = new ArrayList<>(platform.getChildren().filtered(node -> node instanceof Bullet));

        List<Bullet> bulletsToRemove = new ArrayList<>(); // New list to store bullets to remove

        for (Node bullet : bullets) {
            Bullet bulletObj = (Bullet) bullet;
            bulletObj.update();

            if (bulletObj.getBoundsInParent().getMaxY() <= 0) {
                bulletsToRemove.add(bulletObj);
            }
        }

        // Remove bullets after iteration is complete
        javafx.application.Platform.runLater(() -> {
            platform.getChildren().removeAll(bulletsToRemove);
        });
    }
    private void moveEnemy() {
        javafx.application.Platform.runLater(() -> {
            se233.project2.model.Character character = platform.getCharacter();
            Boss boss = platform.getBoss();

            if (hasEnemy) {
                for (Enemy enemy : platform.getEnemy()) {
                    enemy.move(); // Move enemy

                    if (Math.random() < 0.02) {
                        enemy.shoot();
                    }

                    int column = (enemy.getX() - 1) / 30;
                    platform.updateLastEnemy(column, enemy);

                    if (enemy.getY() + Enemy.ENEMY_HEIGHT >= character.getY()) {
                        // Game over condition (enemies reached the bottom)
                        platform.getChildren().removeAll(platform.getEnemy());
                        platform.getChildren().remove(character);
                        platform.showExplosionEffect(character.getX(), character.getY());
                        System.out.println("Game Over");
                        running = false;
                        gameOver.setVisible(true);
                        handleGameOver();
                        return;
                    }
                }
            } else {
                if (boss != null) {
                    boss.move(); // Move boss
                    boss.Shoot();
                }

                // Add logic to update enemies' movement after the boss is removed
                for (Enemy enemy : platform.getEnemy()) {
                    enemy.move(); // Move enemy

                    if (Math.random() < 0.02) {
                        enemy.shoot();
                    }

                    int column = (enemy.getX() - 1) / 30;
                    platform.updateLastEnemy(column, enemy);

                    if (enemy.getY() + Enemy.ENEMY_HEIGHT >= character.getY()) {
                        // Game over condition (enemies reached the bottom)
                        platform.getChildren().removeAll(platform.getEnemy());
                        platform.getChildren().remove(character);
                        platform.showExplosionEffect(character.getX(), character.getY());
                        System.out.println("Game Over");
                        running = false;
                        gameOver.setVisible(true);
                        handleGameOver();
                        return;
                    }
                }
            }
        });
    }
    private void checkBulletCollisions() {
        Platform platform = (Platform) this.platform;
        List<Node> bullets = new ArrayList<>(platform.getChildren().filtered(node -> node instanceof Bullet));

        for (Iterator<Node> iterator = bullets.iterator(); iterator.hasNext();) {
            Bullet bulletObj = (Bullet) iterator.next();

            if (!bulletObj.isEnemyBullet()){
                javafx.application.Platform.runLater(() ->{
                    for (Enemy enemy : platform.getEnemy()) {
                        if (bulletObj.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            // Remove the bullet
                            platform.getChildren().remove(bulletObj);
                            // Remove the enemy
                            platform.getChildren().remove(enemy);
                            platform.getEnemy().remove(enemy);
                            platform.showExplosionEffect(enemy.getX(),enemy.getY());
                            platform.increaseScore(10);

                            if (platform.getEnemy().isEmpty()) {
                                hasEnemy = false; // No enemies in the game
                                spawnBoss(); // Spawn the boss
                            }
                            break;
                        }
                    }
                });
                Boss boss = platform.getBoss();
                javafx.application.Platform.runLater(() -> {
                    if (boss != null && bulletObj.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                        boss.hit();
                        platform.getChildren().remove(bulletObj);
                        if (boss.isDefeated()) {
                            removeBoss(); // Remove boss when defeated
                            platform.increaseScore(100);
                            platform.showExplosionEffect(boss.getX(),boss.getY());
                            platform.displayAnimationText("The boss has been Defeated");
                        }
                    }
                });
            }

            // Check if bullet hits the character
            Character character = platform.getCharacter();
            javafx.application.Platform.runLater(() ->{
                if (bulletObj.getBoundsInParent().intersects(character.getBoundsInParent())) {
                    // Remove the bullet
                    platform.getChildren().remove(bulletObj);

                    // Decrement lives
                    character.setLives(character.getLives() - 1);

                    if (character.getLives() > 0) {
                        platform.getChildren().remove(platform.getLivesList().get(character.getLives()));
                        platform.getChildren().removeAll(bullets);
                        platform.showExplosionEffect(character.getX(),character.getY());

                        // Respawn the character if there are remaining lives
                        platform.getChildren().remove(character);
                        character.setX(character.getStartX());
                        character.setY(character.getStartY());
                        platform.getChildren().add(character);
                    } else {
                        platform.getChildren().remove(platform.getLivesList().get(character.getLives()));

                        // Game over condition (character has no lives remaining)
                        platform.getChildren().remove(character);
                        platform.showExplosionEffect(character.getX(), character.getY());
                        System.out.println("Game Over");
                        running = false;
                        gameOver.setVisible(true);
                        handleGameOver();
                        return;
                    }
                }
            });
            // Ensure the iterator is still valid after potential removals
            if (!iterator.hasNext()) {
                break;
            }
        }
    }
    public void handleGameOver() {
        javafx.application.Platform.runLater(() -> {
            System.out.println("Handle Game Over Called"); // Add this line
            platform.getChildren().removeAll(platform.getEnemy());

            // Remove bullets
            List<Node> bullets = platform.getChildren().filtered(node -> node instanceof Bullet);
            platform.getChildren().removeAll(bullets);

            // Check if there's no boss present and the character still has lives
            if (!platform.hasBoss() && platform.getCharacter().getLives() <= 0) {
                // Handle game over condition (character has no lives remaining)
                System.out.println("Game Over");
                running = false;
                gameOver.setVisible(true);
            }
        });
    }
    public void spawnBoss() {
        if (!hasEnemy && !platform.hasBoss() && !platform.isAnimationInProgress()) {
            platform.getChildren().removeAll(platform.getEnemy()); // Remove existing enemies

            // Display animation text
            platform.displayAnimationText("Get ready for the boss!");

            // Wait for the animation to finish before spawning the boss
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), event -> {
                        Boss boss = new Boss(1, 50, 0, 0); // Adjust coordinates as needed
                        platform.getChildren().add(boss);
                        platform.setBoss(boss); // Add a method to set the boss in the Platform class if needed
                    })
            );
            timeline.play();
        }
    }
    public void removeBoss() {
        Platform platform = (Platform) this.platform;
        platform.getChildren().remove(platform.getBoss());
        platform.setHasBoss(false);
        allEnemiesRemoved = true; // Set the flag to true after boss is removed
        bossDefeated = true; // Set bossDefeated flag to true after boss is removed
    }

    @Override
    public void run() {
        while (running){
            float time = System.currentTimeMillis();
            try {
                update(platform.getCharacter());
                moveBullets();
                moveEnemy();
                checkBulletCollisions();
            } catch (Exception e){
                logger.error("An exception occurred: " + e.getMessage());
                e.printStackTrace();
            }

            time = System.currentTimeMillis() - time;
            javafx.application.Platform.runLater(() -> {
                if (allEnemiesRemoved && !bossAlive) {
                    platform.getChildren().remove(platform.getBoss());
                    allEnemiesRemoved = false; // Reset the flag
                }
                if (bossDefeated && !platform.isAnimationInProgress()) {
                    platform.spawnNewEnemies();
                    bossDefeated = false; // Reset the flag after spawning enemies
                }
            });

            if(time < interval){
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
