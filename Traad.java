
public class Traad implements Runnable {
	Rute rute;
	Rute previousRute;
	String str;
	LIsteMonitor t;
	String name;
//constructor of the traad
	public Traad(Rute r, String s, LIsteMonitor t2, Rute l, String nn) {
		rute = r;
		str = s;
		t = t2;
		previousRute = l;
		name = nn;
	}

	public void run() {
		str = str + " --> " + rute.coordinate();
		Rute check = rute;
		boolean status = false;
		Rute checkP = previousRute;
		//while'rute' still has a valid next step
		//use while loop instead of recursion, since it is easier to debugging
		while (rute.getValidNeighboursNumber().length() > 1) {
			//if there is a valid next step, then just go there, at the same time change status to true
			//as a signal to later directions
			if (check.getNord().tilTegn() == '.' && check.getNord().equals(previousRute) == false) {
				previousRute = rute;
				rute = rute.gaa("n");
				status = true;
			}
			if (check.getSyd().tilTegn() == '.' && check.getSyd().equals(checkP) == false) {
				if (status) {
					//if the status is true, meaning the tradd already choose to go one step towards the north
					//then we call method Sthread to create a new thread and give currentstring to it 
					check.Sthread(check, t, str);
				} else {
					previousRute = rute;
					rute = rute.gaa("s");
					status = true;
				}
			}
			if (check.getVest().tilTegn() == '.' && check.getVest().equals(checkP) == false) {
				if (status) {

					check.Vthread(check, t, str);
				} else {
					previousRute = rute;
					rute = rute.gaa("v");
					status = true;
				}
			}
			if (check.getOst().tilTegn() == '.' && check.getOst().equals(checkP) == false) {
				if (status) {

					check.Ethread(check, t, str);
				} else {
					previousRute = rute;
					rute = rute.gaa("e");
				}
			}
			//at end of each round add coordinate to the string
			//check is to keep the rute not changing value for the later direactions
			//eg, if the tradd has a next step in the north direction, then it goes north, the rute also 
			//changed one step north, check is still remain the same, 
			// so the later direction can still check the rute we want.
			check = rute;
			checkP = previousRute;
			str = str + " --> " + rute.coordinate();

			status = false;

		}
		if (rute.edge()) {
			t.addString(str);
		}
	}

}
