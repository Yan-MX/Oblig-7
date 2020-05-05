
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
// the monitor has a lock to make sure one string addfed at a time
public class LIsteMonitor {
	List<String> list;
	Lock lock = new ReentrantLock();
	public LIsteMonitor(java.util.List<String>  t) {
		list = t;
	}
	

	public void addString(String s) {
		lock.lock();
		list.add(s);
		lock.unlock();
	}
}
