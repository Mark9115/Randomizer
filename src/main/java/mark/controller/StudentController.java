package mark.controller;

import mark.entity.ParseXLSX;
import mark.entity.Rating;
import mark.entity.RatingWrapper;
import mark.entity.Student;
import mark.service.RatingService;
import mark.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final RatingService ratingService;

    static List<Integer> askedIndexes = new ArrayList<>();
    static List<Integer> repliedIndexes = new ArrayList<>();

    static int countMod = 0;

    static LocalDate localDate = LocalDate.now();

    public StudentController(StudentService studentService, RatingService ratingService) {
        this.studentService = studentService;
        this.ratingService = ratingService;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getAllUser(Model model) {
        List<Student> studentRecords = studentService.getAllUsers();
        model.addAttribute("students", studentRecords);
        return "index";
    }

    @RequestMapping("/parse")
    public RedirectView parseStudents() throws IOException {
        ParseXLSX parseXLSX = new ParseXLSX(studentService);
        parseXLSX.parse();
        RedirectView rv = new RedirectView();
        rv.setUrl("/");
        return rv;
    }

    @RequestMapping("/reset")
    public RedirectView reset() {
        askedIndexes.clear();
        repliedIndexes.clear();
        countMod = 0;
        RedirectView rv = new RedirectView();
        rv.setUrl("/");
        return rv;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public RedirectView delete(@PathVariable("id") long id) {
        List<Student> studentRecords = studentService.getAllUsers();
        for (Student student: studentRecords) {
            if(student.getId() == id){
                studentService.deleteStudent(student);
            }
        }

        RedirectView rv = new RedirectView();
        rv.setUrl("/");
        return rv;
    }

    @RequestMapping(value = "/random", method = RequestMethod.POST)
    public String random(Model model,@ModelAttribute RatingWrapper ratingWrapper) {
        List<Student> students = studentService.getAllUsers();

        if (askedIndexes.size() == students.size()) {
            System.out.println();
            return "felicitations";
        }


        Random random = new Random();
        int firstRandom;
        int secondRandom;

        if (askedIndexes.size() == 0) {

            firstRandom = random.nextInt(students.size());
            secondRandom = random.nextInt(students.size());

            while (firstRandom == secondRandom || students.get(firstRandom).getGroupId() == students.get(secondRandom).getGroupId()) {
                secondRandom = random.nextInt(students.size());
            }
        } else {
            firstRandom = repliedIndexes.get(repliedIndexes.size() - 1);
            secondRandom = random.nextInt(students.size());
        }

        boolean br = false;
        if (askedIndexes.size() == students.size() - 1) {
            askedIndexes.add(firstRandom);
            repliedIndexes.add(askedIndexes.get(0));
            secondRandom = askedIndexes.get(0);
            br = true;
        }

        int tempS = secondRandom;
        if (!br) {
            if (!askedIndexes.isEmpty()) {
                if (secondRandom == askedIndexes.get(0)) {
                    while (tempS == secondRandom) {
                        secondRandom = random.nextInt(students.size());
                    }
                }
            }
        }

        while (askedIndexes.contains(firstRandom) || repliedIndexes.contains(secondRandom)) {
            if (br) break;
            int temp = secondRandom;
            while (temp == secondRandom || secondRandom == askedIndexes.get(0)) {
                secondRandom = random.nextInt(students.size());
            }
        }

        if (!br){
            if (students.get(firstRandom).getGroupId() != students.get(secondRandom).getGroupId()) {
                askedIndexes.add(firstRandom);
                repliedIndexes.add(secondRandom);
            } else {
                boolean flag = false;
                for (int i = 0; i < students.size(); i++) {
                    if (!askedIndexes.contains(firstRandom) && !repliedIndexes.contains(i)) {
                        if (i != askedIndexes.get(0)) {
                            if (students.get(firstRandom).getGroupId() != students.get(i).getGroupId()) {
                                askedIndexes.add(firstRandom);
                                repliedIndexes.add(i);
                                secondRandom = repliedIndexes.get(repliedIndexes.size() - 1);
                                flag = true;
                            }
                        }
                    }
                }
                if (!flag) {
                    askedIndexes.add(firstRandom);
                    repliedIndexes.add(secondRandom);
                }
            }
        }


        for (int i = 0; i < askedIndexes.size(); i++) {
            System.out.println(students.get(askedIndexes.get(i)).getId() + " = " + students.get(repliedIndexes.get(i)).getId());
        }
        System.out.println();


        model.addAttribute("firstStudentName", students.get(firstRandom).getName());
        model.addAttribute("firstStudentId", students.get(firstRandom).getId());
        model.addAttribute("secondStudentName", students.get(secondRandom).getName());
        model.addAttribute("secondStudentId", students.get(secondRandom).getId());

        model.addAttribute("bothRatings",new RatingWrapper());

        if(countMod > 0){
            System.out.println(ratingWrapper.getFirstRating());
            System.out.println(ratingWrapper.getSecondRating());
            ratingService.addRating(
                    new Rating(0,students.get(firstRandom).getId(),
                            localDate,ratingWrapper.getFirstRating())
            );

            ratingService.addRating(
                    new Rating(0,students.get(secondRandom).getId(),
                            localDate,ratingWrapper.getSecondRating())
            );
        }

        countMod++;
        return "random";
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public void randomGet(Model model,@ModelAttribute RatingWrapper ratingWrapper) {
        countMod = 0;
        askedIndexes.remove(askedIndexes.size()-1);
        repliedIndexes.remove(repliedIndexes.size()-1);
        random(model,ratingWrapper);
    }

    @RequestMapping(value = "/example")
    public String example(@ModelAttribute Rating rating,Model model){
        model.addAttribute("rating",new Rating());

        System.out.println(rating.getRecord());
        return "exampleForm";
    }

}

