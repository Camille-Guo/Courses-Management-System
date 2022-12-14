package com.example.test.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


import javax.swing.JOptionPane;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.test.model.Course;
import com.example.test.model.NewPass;
import com.example.test.model.OldPass;
import com.example.test.model.SearchPara;
import com.example.test.model.User;
import com.example.test.repo.CourseRepo;
import com.example.test.repo.UserRepo;
import com.example.test.service.CourseService;
import com.example.test.service.UserService;

@Controller
public class AppController {

	@Autowired
	private UserService service;
	
	@Autowired
	private CourseService courseservice;
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CourseRepo courseRepo;

	@RequestMapping("")
	public String viewHomePage(Model model) {

		return "home";
	}

	@RequestMapping("/signup")
	public String signupPage(Model model) {
		User user = new User();

		model.addAttribute("user", user);
		return "userRegistration";
	}

	@RequestMapping("/login-allot")
	public String loginAllot() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = service.getUserByEmail();
		
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

		String strAuthorities = authorities.toString();
		if (strAuthorities.contains("student")) {
			if (user.getExpireDate().isBefore(LocalDate.now())) {
				JOptionPane.showMessageDialog(null, "Your status is already expired, do you want to sign up again?");
				return "redirect:/home/";
			}
			return "redirect:/student";
		} else if (strAuthorities.contains("teacher")) {

			return "redirect:/teacher";
		} else {

			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        System.out.println("sing up =====");
        
        List<User> listUsers = service.getAllUsers();
        
        for (User l : listUsers) {
        	if (l.getEmail().contentEquals(user.getEmail())){
        		String msg = "You have already registered.";
        		model.addAttribute("msg", msg);
        		model.addAttribute("user", user);
        		return "userRegistration";
        	}
        }
        
        if (bindingResult.hasErrors()) {
			return "userRegistration";
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		 
		service.saveUser(user);
        
        
        return "redirect:/login";
        
	}
	
	
	@RequestMapping(value = "/addUserPage")
	public String showAddUserPage(Model model) {
		
		User user = new User();
		model.addAttribute("user", user);
		return "addUser";
	}
	
	
	

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		
		System.out.println("render");
          String type = user.getType();
        
        List<User> listUsers = service.getAllUsers();
        System.out.println("render");
        for (User l : listUsers) {
        	if (l.getEmail().contentEquals(user.getEmail())){
        		String msg = "This user have already registered.";
        		model.addAttribute("msg", msg);
        		model.addAttribute("user", user);
        		return "addUser";
        	}
        }
        System.out.println("render");
        if (bindingResult.hasErrors()) {
			return "addUser";
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		System.out.println("render");
		 
		service.saveUser(user);
        
        System.out.println("render");
        return "redirect:/admin/" + type;
	}


	@RequestMapping("/student")
	public String studentPage(Model model) {
		User user = service.getUserByEmail();
		model.addAttribute("student", user);
		return "student";
	}
	
	@RequestMapping("/teacher")
	public String teacherPage(Model model) {
		User user = service.getUserByEmail();
		model.addAttribute("teacher", user);
		return "teacher";
	}

	@RequestMapping("/admin")
	public String adminPage(Model model) {
		User user = service.getUserByEmail();
		model.addAttribute("admin", user);
		return "admin";
	}

	@RequestMapping("/admin/student")
	public String adminStudentPage(Model model) {
		SearchPara searchPara = new SearchPara();
		model.addAttribute("searchPara", searchPara);
		List<User> listStudents = service.listAllStudents();
		model.addAttribute("listStudents", listStudents);
		return "admin_student";
	}

	@RequestMapping("/admin/teacher")
	public String adminTeacherPage(Model model) {
		String flag="found";
		SearchPara searchPara = new SearchPara();
		model.addAttribute("searchPara", searchPara);
		List<User> listTeachers = service.listAllTeachers();
		model.addAttribute("listTeachers", listTeachers);
		model.addAttribute("flag", flag);
		return "admin_teacher";
	}

	@RequestMapping("/admin/courses")
	public String adminCoursePage(Model model) {
		SearchPara searchPara = new SearchPara();
		model.addAttribute("searchPara", searchPara);
		List<Course> listCourses = service.listAllCourses();
		model.addAttribute("listCourses", listCourses);
		return "admin_courses";
	}

	@RequestMapping("/admin/records")
	public String adminRecordPage(Model model) {
		String flag = "student";
		model.addAttribute("flag", flag);
		SearchPara searchPara = new SearchPara();
		model.addAttribute("searchPara", searchPara);
		List<User> listRecords = service.listRecords();
		model.addAttribute("listRecordsUsers", listRecords);

		return "admin_records";
	}

	@RequestMapping("/addCourse")
	public String viewAddCourse(Model model) {
		Course course = new Course();
		model.addAttribute("course", course);
		return "admin_add_edit_course";
	}

	@RequestMapping(value = "/saveCourse", method = RequestMethod.POST)
	public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "admin_add_edit_course";
		}

		service.saveCourse(course);

		return "redirect:/admin/courses";
	}
	
	@RequestMapping(value = "/editCourse/{id}")
	public ModelAndView showEditCoursePage(@PathVariable(name = "id") String id) {
		ModelAndView mav = new ModelAndView("admin_add_edit_course");
		Course course = service.getCourse(id);
		mav.addObject("course", course);
		return mav;

	}

	@RequestMapping(value = "/updateCourse", method = RequestMethod.POST)
	public String updateCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			return "admin_add_course";
		}
		service.saveCourse(course);
		return "redirect:/admin/courses";
	}

	@RequestMapping(value = "deleteCourse/{id}")
	public String deleteCourse(@PathVariable(name = "id") String id) {

		service.deleteCourse(id);
		return "redirect:/admin/courses";
	}

	@RequestMapping(value = "deleteUser/{id}")
	public String deleteUser(@PathVariable(name = "id") Long id) {
		User user = service.getUser(id);
		List <Course> listCourse = user.getCourse();
		
		for  ( Course c : listCourse) {
			c.getUsers().remove(user);
		}
		for  ( Course c : listCourse) {
			service.saveCourse(c);
		}
		
		String type = user.getType();
		service.deleteUser(id);
		return "redirect:/admin/" + type;
	}
	
	@RequestMapping(value = "/searchOneCourse", method = RequestMethod.POST)
	public String searchOneCourse(@ModelAttribute("searchPara") SearchPara searchPara, Model model) {
		System.out.println("try to find");
		Course course = new Course();
		try {
			course = service.getOneCourse(searchPara.getParaValue());
		} catch (NoSuchElementException e) {
			String msg = "No result found!";
			model.addAttribute("msg", msg);
			List<Course> listCourses = service.listAllCourses();
			model.addAttribute("listCourses", listCourses);

			model.addAttribute("searchPara", searchPara);
			return "admin_course";
		}
		List<Course> listCourses = new ArrayList<>();
		listCourses.add(course);
		model.addAttribute("listCourses", listCourses);
		model.addAttribute("searchPara", searchPara);
		return "admin_courses";

	}

	@RequestMapping(value = "/searchOneStudent", method = RequestMethod.POST)
	public String searchOneStudent(@ModelAttribute("searchPara") SearchPara searchPara, Model model) {

		User user = service.getOneUser(searchPara.getParaValue());
		if (user == null) {
			String msg = "No result found!";
			model.addAttribute("msg", msg);
			List<User> listUsers = service.listAllStudents();
			model.addAttribute("listStudents", listUsers);

			model.addAttribute("searchPara", searchPara);
			return "admin_student";
		}

		List<User> listUsers = new ArrayList<>();
		listUsers.add(user);
		model.addAttribute("listStudents", listUsers);
		model.addAttribute("searchPara", searchPara);
		return "admin_student";
	}

	@RequestMapping(value = "/searchOneTeacher", method = RequestMethod.POST)
	public String searchOneTeacher(@ModelAttribute("searchPara") SearchPara searchPara, Model model) {
		User user = service.getOneUser(searchPara.getParaValue());
        String flag = "";
        System.out.println(user);
		if (user == null) {
			String msg = "No result found!";
			model.addAttribute("msg", msg);
			model.addAttribute("flag", flag);
			model.addAttribute("searchPara", searchPara);
			return "admin_teacher";
		}
		flag="found";
		List<User> listUsers = new ArrayList<>();
		listUsers.add(user);
		model.addAttribute("listTeachers", listUsers);
		model.addAttribute("searchPara", searchPara);
		model.addAttribute("flag", flag);
		return "admin_teacher";
	}
	

	@RequestMapping(value = "/searchOneRecord", method = RequestMethod.POST)
	public String searchOneRecord(@ModelAttribute("searchPara") SearchPara searchPara, Model model) {
		String flag;
        String msg = " ";
		System.out.println("by student?" + searchPara.getParaCategory());
		if (searchPara.getParaCategory().contentEquals("student")) {
			System.out.println("by student");
			flag = "student";
			List<User> listRecords = service.getByStudent(searchPara.getParaValue());
			if (listRecords.size() > 0) {
				System.out.println("> 0");
				model.addAttribute("listRecordsUsers", listRecords);
				model.addAttribute("searchPara", searchPara);
				model.addAttribute("flag", flag);
				model.addAttribute("msg", msg);
				return "admin_records";
			}	 
		}

		flag = "course";
		List<Course> listCourses = service.getByCourse(searchPara.getParaValue());
		if (listCourses.size() > 0) {
			model.addAttribute("listRecordsCourses", listCourses);
			model.addAttribute("searchPara", searchPara);
			model.addAttribute("flag", flag);
			model.addAttribute("msg", msg);
			return "admin_records";
		} 
	    msg = "No results found...";
		model.addAttribute("msg", msg);
		return "admin_records";
		
	}

	
//	@RequestMapping("/student/coursesOverview")
//	public String studentAllCoursePage(Model model) {
//		SearchPara searchPara = new SearchPara();
//		model.addAttribute("searchPara", searchPara);
//		List<Course> listCourses = service.listAllCourses();
//		model.addAttribute("listCourses", listCourses);
//		return "student_courses_overview";
//	}

//	@RequestMapping("/student/selectedCourses")
//	public String studentSelectedCoursePage(Model model) {
//		User user = service.getUserByEmail();   
//		List<Course> listCourses = user.getCourse();
//		model.addAttribute("listCourses", listCourses);
//		return "student_selected_courses";
//	}

//	@RequestMapping("/selectCourse/{id}")
//	public String addSelectedCourse(Model model, @PathVariable(name = "id") String id) {
//		
//		System.out.println("select:-------------");
//		Course course = service.getCourse(id);
//		User user = service.getUserByEmail();
//		List<Course> listCourse = user.getCourse();
//		if (listCourse.contains(course)) {
//			System.out.print(course.getCourseName());
//			String msg = "You have already selected course:" + course.getCourseName();
//			model.addAttribute("msg", msg);
//			SearchPara searchPara = new SearchPara();
//			model.addAttribute("searchPara", searchPara);
//			List<Course> listCourses = service.listAllCourses();
//			model.addAttribute("listCourses", listCourses);
//			return "student_courses_overview";
//		}
//		
//		listCourse.add(course);
//		user.setCourse(listCourse);
//		List<User> listUsers = course.getUsers();
//		listUsers.add(user);
//
//		Integer spotsLeft = course.getSpotsLeft();
//		course.setSpotsLeft(spotsLeft - 1);
//
//		course.setUsers(listUsers);
//		service.saveUser(user);
//		service.saveCourse(course);
//		return "redirect:/student/selectedCourses";
//	}
//
//	@RequestMapping("/deletSelectCourse/{id}")
//	public String deletSelectedCourse(Model model, @PathVariable(name = "id") String id) {
//		Course course = service.getCourse(id);
//		User user = service.getUserByEmail();
//		List<Course> listCourse = user.getCourse();
//		listCourse.remove(course);
//		Integer spotsLeft = course.getSpotsLeft();
//		course.setSpotsLeft(spotsLeft + 1);
//		user.setCourse(listCourse);
//		service.saveUser(user);
//		return "redirect:/student/selectedCourses";
//	}
//
//	@RequestMapping("/addSelectCourse")
//	public String addSelectedCourse() {
//
//		return "redirect:/student/coursesOverview";
//	}

	@RequestMapping("/teacher/courses_in_charge")
	public String showCoursesInChargePage(Model model) {
		User user = service.getUserByEmail();
		List<Course> listCourses = service.getCourseInCharge(user.getName());
		model.addAttribute("listCourses", listCourses);
		return "teacher_courses_in_charge";
	}

	@RequestMapping("/teacher/students_list")
	public String showStudentListPage(Model model) {
       List<User> listStudents= new ArrayList<>();
       SearchPara searchPara = new SearchPara();
        model.addAttribute("listStudents", listStudents);
        model.addAttribute("searchPara", searchPara);
		return "teacher_student_list";
	}

	
	@RequestMapping("/teacher/search_students_list")
	public String showStudentListOfOneCourse(Model model, @ModelAttribute("searchPara") SearchPara searchPara) {
		Course course = service.getOneCourse(searchPara.getParaValue());
		List<User> listUsers = course.getUsers();
 
		System.out.println(listUsers);
		if (listUsers.size() == 0) {
			String msg = "No results found";
			model.addAttribute("msg", msg);
			 model.addAttribute("searchPara", searchPara);
			return "teacher_student_list";
		}
		model.addAttribute("listStudents", listUsers);
		String flag = "course";
		model.addAttribute("flag", flag);
		 model.addAttribute("searchPara", searchPara);
		return "teacher_student_list";
	}
	
	

	@RequestMapping(value = "/editUser/{id}")
	public ModelAndView showEditUserPage(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("edit_user");
		User user = service.getUser(id);
		mav.addObject("user", user);
		return mav;

	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {

			return "edit_user";
		}

		service.saveUser(user);
		String type = user.getType();
		User currentUser = service.getUserByEmail();
		if (currentUser.getType().contentEquals("admin") && type.contentEquals("admin")) {
			return "redirect:/admin";
		} else if (!currentUser.getType().contentEquals("admin")) {
			return "redirect:/" + type;
		} 
		return "redirect:/admin/" + type;
	}

	@RequestMapping(value = "/modifyPass")
	public String showModifyPassPage(Model model) {

		OldPass oldPass = new OldPass();
		model.addAttribute("oldPass", oldPass);

		return "modifyPass";

	}

	@RequestMapping(value = "/verifyOldPass", method = RequestMethod.POST)
	public String verifyOldPass(@ModelAttribute("oldPass") OldPass oldPass, Model model) {
		User user = service.getUserByEmail();

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if (!passwordEncoder.matches(oldPass.getOriginalPassword(), user.getPassword())) {
			String msg = "Incorrect original password, please try again!";
			model.addAttribute("msg", msg);
			model.addAttribute("oldPass", oldPass);
			return "modifyPass";
		}
		NewPass newPass = new NewPass();
		model.addAttribute("newPass", newPass);
		return "resetPass";
	}

	@RequestMapping(value = "/updatePass")
	public String updatePass(@ModelAttribute("newPass") NewPass newPass, Model model) {
		User user = service.getUserByEmail();

		if (!newPass.getNewPassword().contentEquals(newPass.getNewRetypePassword())) {
			String msg = "passwords not match!";
			model.addAttribute("msg", msg);
			return "resetPass";
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPass.getNewPassword());
		user.setPassword(encodedPassword);
		service.saveUser(user);
		return "redirect:/login";
	}

	
	// Zibin 
	
	
	//course overview
		@GetMapping("/student_courses_overview")
		public String listCourses(Model model) {
			User user = service.getUserByEmail();
			List<Course> listCourses = courseservice.listAll();
			model.addAttribute("listCourses", listCourses);
			model.addAttribute("user", user);
			return "student_courses_overview";
		}

		// search course
		@RequestMapping(value = "/search_course", method = RequestMethod.POST)
		public String searchCourse(@RequestParam(value = "search_field", required = false) String search_field,Model model) {
			List<Course> listCourses = courseservice.listSearchCourse(search_field);
			model.addAttribute("listCourses", listCourses);
			User user = service.getUserByEmail();
			model.addAttribute("user", user);
			return "student_courses_overview";
		}

		
		// select course
		@RequestMapping("/select_course/{courseId}")
		public String seletcCourse(@PathVariable(name = "courseId") String id,Model model) {
			Course course = courseservice.get(id);
			User user = service.getUserByEmail();

			Date now = new Date();
			if (course.getDeadline().before(now)) {
				String msg = "Select Course Fail!Because exceeded the deadline!";
				model.addAttribute("msg", msg);
				List<Course> listCourses = courseservice.listAll();
				model.addAttribute("listCourses", listCourses);
				model.addAttribute("user", user);
				return "student_courses_overview";
			}

			if (course.getSpotsLeft() < 1) {
				String msg = "Select Course Fail!Because no spots available!";
				model.addAttribute("msg", msg);
				List<Course> listCourses = courseservice.listAll();
				model.addAttribute("listCourses", listCourses);
				model.addAttribute("user", user);
				return "student_courses_overview";
			}

			Collection<Course> course1 = user.getCourse();
			if (course1.contains(course)) {
				String msg = "Select Course Fail!Because you already selected it!";
				model.addAttribute("msg", msg);
				List<Course> listCourses = courseservice.listAll();
				model.addAttribute("listCourses", listCourses);
				model.addAttribute("user", user);
				return "student_courses_overview";
			}

			course.setSpotsLeft(course.getSpotsLeft() - 1);
			user.getCourse().add(course);
			course.getUsers().add(user);
			userRepo.save(user);
			courseRepo.save(course);
			return "redirect:/selected_course";
		}

		// selected course
		@RequestMapping("/selected_course")
		public String selectedCourse(Model model) {
			User user = service.getUserByEmail();
			Collection<Course> listSelectedCourses = user.getCourse();
			model.addAttribute("listSelectedCourses", listSelectedCourses);
			model.addAttribute("user", user);
			return "student_selected_courses";
		}

		// delete course
		@RequestMapping("/delete_course/{courseId}")
		public String deletcCourse(@PathVariable(name = "courseId") String id) {
			Course course = courseservice.get(id);
			User user = service.getUserByEmail();
			course.setSpotsLeft(course.getSpotsLeft() + 1);
			user.getCourse().remove(course);
			course.getUsers().remove(user);
			userRepo.save(user);
			courseRepo.save(course);
			return "redirect:/selected_course";
		}


	
	
	
	
}
	

