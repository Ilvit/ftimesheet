package com.timesheet;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.AppRole;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.UserSavedSheetLines;
import com.timesheet.enums.Positions;
import com.timesheet.enums.RolesNames;
import com.timesheet.repositories.AppRoleRepository;
import com.timesheet.repositories.UserSavedSheetLineRepository;
import com.timesheet.services.AppUserService;
import com.timesheet.services.EmployeeService;
import com.timesheet.services.SheetdayService;

@SpringBootApplication
public class TimesheetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetServiceApplication.class, args);
	}	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean 
	CommandLineRunner clr(EmployeeService employeeService, SheetdayService sheetdayService, AppRoleRepository appRoleRepository,
			UserSavedSheetLineRepository savedSheetLineRepository, AppUserService appUserService) {
		return  args->{
			//Add employees
			employeeService.addNewEmployee("KAPENDA", "KAKESE", "Patient", "kapendak2020@gmail.com", Positions.DATA);
			employeeService.addNewEmployee("KYUNGU", "MWANANGOY", "Tresor", "mwanangoytresor@gmail.com", Positions.SITE_SUPERVISOR);
			employeeService.addNewEmployee("NGOIE", "", "Didier", "didierngoie@maisonfaradja.org", Positions.COP);
			employeeService.addNewEmployee("KABWE", "NGOIE", "Delly", "dellyngoie@maisonfaradja.org", Positions.DCOP);
			employeeService.addNewEmployee("TSHIKUT", "NAWEJ", "Heritier", "tnawej@gmail.com", Positions.DATA);
			employeeService.addNewEmployee("NGUZA", "TSHANDA", "Declerk", "nguzadeclerk@hotmail.com", Positions.DATA);
			employeeService.addNewEmployee("ILUNGA", "MUTOMBO", "Joseph", "joseph2024@gmail.com", Positions.DATA);
//			Add roles
			for(RolesNames roleName:RolesNames.values()) {
				appRoleRepository.save(new AppRole(null,roleName.name()));
			}
			//define a supervisor
			employeeService.setSupervisorToEmployee("KYUNGU", "NGUZA");
			employeeService.setSupervisorToEmployee("KYUNGU", "TSHIKUT");
			employeeService.setSupervisorToEmployee("NGOIE", "ILUNGA");
			employeeService.setSupervisorToEmployee("NGOIE", "KABWE");
//			Add users
			appUserService.addUser(new AppUser("Didier", "12345", employeeService.getEmployeeID("NGOIE")));
			appUserService.addUser(new AppUser("Delly", "12345", employeeService.getEmployeeID("KABWE")));
			appUserService.addUser(new AppUser("Declerck", "12345", employeeService.getEmployeeID("NGUZA")));
//			Add roles to users
			appUserService.addRoleToUser("Didier", "USER");		
			appUserService.addRoleToUser("Delly", "USER");	
			appUserService.addRoleToUser("Declerck", "USER");		
			//Creating a timesheet line
			TimesheetPeriods tp=new TimesheetPeriods();
			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getCurrentPeriod(), employeeService.getEmployeeID("NGOIE"), "");
			sheetdayService.saveTimesheetLine(sheetdays);
			UserSavedSheetLines userSavedSl=new UserSavedSheetLines(null, employeeService.getEmployeeID("NGOIE"), tp.getCurrentPeriod(), false, false);
			savedSheetLineRepository.save(userSavedSl);
		}; 
	}	 
}
