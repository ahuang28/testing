//import Student.*;
//import SLinkedList.*;
//import java.util.Arrays;

public class Course {
    public String code;
    public int capacity;
    public SLinkedList<Student>[] studentTable;
    public int size;
    public SLinkedList<Student> waitlist;


    public Course(String code) {
        this.code = code;
        this.studentTable = new SLinkedList[10];
        this.size = 0;
        this.waitlist = new SLinkedList<Student>();
        this.capacity = 10;
    }

    public Course(String code, int capacity) {
        this.code = code;
        this.studentTable = new SLinkedList[capacity];
        this.size = 0;
        this.waitlist = new SLinkedList<>();
        this.capacity = capacity;
    }

    public void changeArrayLength(int m) {
        // creates a new student table for this course
        // creates a new array of SLinkedList<Student>
        SLinkedList<Student>[] diff_length_array = new SLinkedList[m]; //why don't I need <Student> when initializing?

        for (SLinkedList<Student> list : studentTable) {
            if (list == null) {
                continue;
            }
            for (int i = 0; i < list.size(); i++) {
                Student stu = list.get(i);
                int pos = stu.id % m;

                if (diff_length_array[pos] == null) {
                    diff_length_array[pos] = new SLinkedList<Student>();
                }
                diff_length_array[pos].addLast(stu);
            }
        }
        studentTable = diff_length_array;
        this.capacity = m;
    }

    public boolean put(Student s) {
        // return true if method successfully registers the student or adds student onto waitlist
        int c = this.capacity;
        int pos = s.id % c;

        // student already registered in class / waitlist --> False
        /*if (this.get(s.id) != null) {
            return false;
        }*/
        if (s.isRegisteredOrWaitlisted(this.code)) {
            return false;
        }

        // student already has three courses --> False
        if (s.courseCodes.size() == 3) {
            return false;
        }

        // add student to waitlist
        if (this.getCourseSize() == this.capacity && waitlist.size() < (int) studentTable.length/2) {
            waitlist.addLast(s);
            s.addCourse(this.code);
            return true;
        } else if (this.getCourseSize() == this.capacity && waitlist.size() >= (int) studentTable.length/2){
            //made new array 1.5 times bigger
            int new_array_length = (int)(studentTable.length * 1.5);
            this.changeArrayLength(new_array_length);

            //put all students on waitlist into list
            while (waitlist.size() > 0) {
                Student waitstu = waitlist.get(0);
                int new_pos = waitstu.id % studentTable.length;
                if (studentTable[new_pos] == null) {
                    studentTable[new_pos] = new SLinkedList<Student>();
                }
                studentTable[new_pos].addLast(waitstu);
                this.size += 1;
                //remove all students from waitlist
                waitlist.remove(waitstu);

            }
            //add new student to the waitlist
            waitlist.addLast(s);
            s.addCourse(this.code);
            return true;
        }

        if (studentTable[pos] == null) {
            studentTable[pos] = new SLinkedList<Student>();
        }
        studentTable[pos].addLast(s);
        s.addCourse(this.code);
        this.size += 1;
        return true;

    }

    public Student get(int id) {
        // returns a Student, null if student is not registered for the course or waitlist
        int pos = id % studentTable.length;
        if (studentTable[pos] == null) {
            return null;
        }
        for (int i = 0; i < studentTable[pos].size(); i++) {
            Student stu = studentTable[pos].get(i);
            if (stu.id == id) {
                return stu;
            }
        }
        // check waitlist too
        for (int j = 0; j < waitlist.size(); j++) {
            Student waitstu = waitlist.get(j);
            if (waitstu.id == id) {
                return waitstu;
            }
        }
        return null;
    }

        public Student remove(int id) {
            // removes student with this id
            int c = this.capacity;
            int pos = id % c;
            if (studentTable[pos] != null) {
                for (int i = 0; i < studentTable[pos].size(); i++) {
                    Student stu = studentTable[pos].get(i);
                    if (stu.id == id) {
                        studentTable[pos].remove(stu);
                        //drop course to student
                        stu.dropCourse(this.code);
                        this.size -= 1;
                        //move person from waitlist to register
                        if (waitlist.size() != 0) {
                            studentTable[pos].addLast(waitlist.get(0));
                            //waitlist kid add course
                            // dont need this if add course when in waitlist
                            // ???waitlist.get(0).addCourse(this.code);
                            this.size += 1;
                            //remove that same person from waitlist
                            waitlist.remove(waitlist.get(0));
                        }
                        return stu;
                    }
                }
            }

            for (int j = 0; j < waitlist.size(); j++) {
                Student waitstu = waitlist.get(j);
                if (waitstu.id == id) {
                    waitlist.remove(waitstu); //j
                    //DROP
                    waitstu.dropCourse(this.code);
                    return waitstu;
                }
            }
            // returns null if there is no Student associated with id in the class
            return null;
    }


        public int getCourseSize() {
            // insert your solution here and modify the return statement
            /*int num_students = 0;
            for (SLinkedList<Student> list : studentTable) {
                if (list == null) {
                    continue;
                }
                for (int i = 0; i < list.size(); i++) {
                    num_students += 1;

                }
            }
            return num_students;*/
            return this.size;
        }


        public int[] getRegisteredIDs() {
            // return an array of int[] with registered student ids
            // length of array is size (number of students) in course
            int[] registeredIDs = new int[this.getCourseSize()];
            int index = 0;
            for (SLinkedList<Student> list : studentTable) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Student stu = list.get(i);
                        registeredIDs[index] = stu.id;
                        index += 1;
                    }
                }
            }
            return registeredIDs;
        }

        public Student[] getRegisteredStudents() {
            // return an array of type Student[], the registered Students
            // length of array is the current size of the course
            Student[] registeredStuds = new Student[this.capacity];
            int index = 0;
            for (SLinkedList<Student> list : studentTable) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Student stu = list.get(i);
                        registeredStuds[index] = stu;
                        index += 1;
                    }
                }
            }
            return registeredStuds;
        }

        public int[] getWaitlistedIDs() {
            // returns an array of type int[] with ids of students in waitlist
            int[] waitlistIDs = new int[waitlist.size()];
            int index = -1;
            for (int i = 0; i < waitlist.size(); i++) {
                Student waitstu = waitlist.get(i);
                index += 1;
                waitlistIDs[index] = waitstu.id;
            }
            return waitlistIDs;
        }

        public Student[] getWaitlistedStudents() {
            // returns an array of Students in waitlist
            Student[] waitlistStuds = new Student[waitlist.size()];
            int index = -1;
            for (int i = 0; i < waitlist.size(); i++) {
                Student waitstu = waitlist.get(i);
                index += 1;
                waitlistStuds[index] = waitstu;
            }
            return waitlistStuds;
        }

        public String toString() {
            String s = "Course: "+ this.code +"\n";
            s += "--------\n";
            for (int i = 0; i < this.studentTable.length; i++) {
                s += "|"+i+"     |\n";
                s += "|  ------> ";
                SLinkedList<Student> list = this.studentTable[i];
                if (list != null) {
                    for (int j = 0; j < list.size(); j++) {
                        Student student = list.get(j);
                        s +=  student.id + ": "+ student.name +" --> ";
                    }
                }
                s += "\n--------\n\n";
            }

            return s;
        }



}

