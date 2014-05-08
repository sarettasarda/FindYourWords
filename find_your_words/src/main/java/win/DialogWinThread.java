package win;

public class DialogWinThread extends Thread
{
	private volatile Thread blinker;
	int i=0;

	public void start()
	{
		blinker = new Thread(this);
		blinker.start();
	}
	 
    public void stopIt()
    {
        blinker = null;
    }

    public void run() 
    {
        Thread thisThread = Thread.currentThread();
        while (blinker == thisThread) 
        {
        	DialogWin.image.updatePosition(i++);
			try 
			{
				sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
        }
    }
}
