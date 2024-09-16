/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.alunos.github;

/**
 *
 * @author 14851633661
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGameConsole {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 10;
    private static final int INIT_LENGTH = 3;
    private static final char EMPTY = ' ';
    private static final char SNAKE = '#';
    private static final char FOOD = '*';

    private ArrayList<Point> snake;
    private Point food;
    private int direction; // 0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
    private boolean isRunning;
    private Timer timer;
    private Scanner scanner;

    public SnakeGameConsole() {
        snake = new ArrayList<>();
        for (int i = INIT_LENGTH - 1; i >= 0; i--) {
            snake.add(new Point(i, HEIGHT / 2));
        }
        direction = 1; // Start moving to the right
        spawnFood();
        isRunning = true;
        scanner = new Scanner(System.in);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    moveSnake();
                    checkCollision();
                    checkFood();
                    render();
                }
            }
        }, 0, 500); // Update every 500 milliseconds
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT);
        food = new Point(x, y);
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();

        switch (direction) {
            case 0: // UP
                newHead.translate(0, -1);
                break;
            case 1: // RIGHT
                newHead.translate(1, 0);
                break;
            case 2: // DOWN
                newHead.translate(0, 1);
                break;
            case 3: // LEFT
                newHead.translate(-1, 0);
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            isRunning = false;
            timer.cancel();
            System.out.println("Game Over! You hit the wall.");
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isRunning = false;
                timer.cancel();
                System.out.println("Game Over! You collided with yourself.");
                return;
            }
        }
    }

    private void checkFood() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            spawnFood();
        }
    }

    private void render() {
        char[][] board = new char[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[y][x] = EMPTY;
            }
        }

        for (Point p : snake) {
            board[p.y][p.x] = SNAKE;
        }
        board[food.y][food.x] = FOOD;

        System.out.print("\033[H\033[2J"); // Clear console
        System.out.flush();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
        System.out.println("Use WASD to move. Current direction: " + getDirectionString(direction));
        System.out.print("Enter your move (W/A/S/D): ");
        String move = scanner.nextLine().toUpperCase();

        switch (move) {
            case "W":
                if (direction != 2) direction = 0; // UP
                break;
            case "D":
                if (direction != 3) direction = 1; // RIGHT
                break;
            case "S":
                if (direction != 0) direction = 2; // DOWN
                break;
            case "A":
                if (direction != 1) direction = 3; // LEFT
                break;
        }
    }

    private String getDirectionString(int direction) {
        switch (direction) {
            case 0: return "UP";
            case 1: return "RIGHT";
            case 2: return "DOWN";
            case 3: return "LEFT";
            default: return "UNKNOWN";
        }
    }

    public static void main(String[] args) {
        new SnakeGameConsole();
    }
}

