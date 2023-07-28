public class Update extends Thread {

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                update();
                sleep(1800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
    }
}
