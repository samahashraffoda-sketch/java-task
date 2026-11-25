import java.util.*;

class Task_CinemaBooking {

    // A seat in the cinema hall, for example B5 = row B, seat number 5.
    static class Seat {
        final char row;
        final int number;

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // Two Seat objects represent the same seat when row and number match.
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Seat)) {
                return false;
            }
            Seat other = (Seat) o;
            return this.row == other.row && this.number == other.number;
        }

        // Equal seats must have the same hash code.
        @Override
        public int hashCode() {
            return Objects.hash(row, number);
        }
    }

    public static void main(String[] args) {

        // Key = Seat, Value = name of the customer who booked it
        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A: book these seats using put()
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        // Part B: who booked seat C3? Use get().
        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        // Part C: are seats A2 and E6 taken? Use containsKey().
        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        // Part D: Youssef wants seat B5. Do not replace an existing booking.
        String existingCustomer = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (existingCustomer == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + existingCustomer);
        }

        // Part E: Ali moves from seat A1 to seat D2.
        String ali = bookings.remove(new Seat('A', 1));
        if (ali != null) {
            bookings.put(new Seat('D', 2), ali);
        }
        System.out.println("After Ali moved:    " + bookings);

        // Part F: seats E1 and E2 are broken. Hana wants E1.
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));

        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G: how many seats are booked?
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN - prints the seating chart
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change. Prints the hall: X = booked, # = broken, . = free
    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
QUESTIONS - answers

Q1. Why did we have to override hashCode() and equals()?
Because Seat is a class, so Java's default equals() compares object identity.
We want two different Seat objects with the same row and number to represent
the same seat. equals() defines that equality, and hashCode() makes equal
Seat objects produce the same hash code so HashMap and HashSet can find them.

Q2. Delete hashCode() but keep equals(). What happens? Why?
The program may give incorrect results: a newly created Seat that is equal
according to equals() can have a different identity-based hash code. HashMap
and HashSet use the hash code to find the bucket before checking equals(),
so they may fail to find the existing seat. It also violates the equals()
and hashCode() contract.

Q3. Why are row and number final? What could go wrong if number could be changed
after it was put in the map?
They identify the seat and should not change. If a key's fields used by
equals() or hashCode() change after insertion, its hash code may change.
The map may then look in a different bucket and fail to find or remove the key.

Q4. Change equals(Object o) to equals(Seat o). What happens?
Then also remove @Override and run again. What happens now?
With @Override, compilation fails because equals(Seat) does not override
Object.equals(Object). If @Override is removed, it compiles, but it only
overloads equals(); it does not override Object.equals(Object). HashMap and
HashSet use Object.equals(Object), so the custom comparison is not used and
the behavior is like the default identity comparison.

BONUS. Replace the whole Seat class with:
    record Seat(char row, int number) {}
A record automatically provides equals(), hashCode(), and toString()
based on its components. Therefore, two Seat records with the same row
and number compare as equal and work correctly as HashMap keys and
HashSet elements without writing those methods manually.
*/
