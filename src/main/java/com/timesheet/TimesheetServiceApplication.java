package com.timesheet;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Role;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.entities.USAIDProject;
import com.timesheet.enums.Positions;
import com.timesheet.enums.RolesNames;
import com.timesheet.repositories.RoleRepository;
import com.timesheet.repositories.TimesheetSaverRepository;
import com.timesheet.repositories.USAIDProjectRepository;
import com.timesheet.services.AppUserService;
import com.timesheet.services.EmployeeService;
import com.timesheet.services.HolidayService;
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
	CommandLineRunner clr(EmployeeService employeeService, SheetdayService sheetdayService, RoleRepository appRoleRepository,USAIDProjectRepository projectRepository,
			TimesheetSaverRepository savedSheetLineRepository, AppUserService appUserService, HolidayService holidayService) {
		return  args->{
			//Holidays
			/*
			 * holidayService.addHoliday(LocalDate.parse("lundi_18-mars-2024",
			 * TimesheetPeriods.dtf), "Faradja's USAID-ULINZI day");
			 * holidayService.addHoliday(LocalDate.parse("dimanche_30-juin-2024",
			 * TimesheetPeriods.dtf), "DRC independence day");
			 * holidayService.addHoliday(LocalDate.parse("jeudi_01-août-2024",
			 * TimesheetPeriods.dtf), "DRC day of the dead");
			 */
			//Projects
			projectRepository.save(new USAIDProject(null,"USAID-ULINZI_2030"));
			projectRepository.save(new USAIDProject(null,"USAID-EPIC"));
			projectRepository.save(new USAIDProject(null,"ARPA"));
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
				appRoleRepository.save(new Role(null,roleName.name()));
			}
			//define a supervisor
			
			employeeService.setSupervisorToEmployee("KYUNGU", "TSHIKUT");
			employeeService.setSupervisorToEmployee("NGOIE", "ILUNGA");
			employeeService.setSupervisorToEmployee("NGOIE", "KABWE");
			employeeService.setSupervisorToEmployee("NGOIE", "NGOIE");
			employeeService.setSupervisorToEmployee("KABWE", "NGUZA");
			
//			Add users
			appUserService.saveUser(new AppUser("Didier", "12345", employeeService.getEmployeeID("NGOIE")));
			appUserService.saveUser(new AppUser("Delly", "12345", employeeService.getEmployeeID("KABWE")));
			appUserService.saveUser(new AppUser("Declerck", "12345", employeeService.getEmployeeID("NGUZA")));
//			Add roles to users
			appUserService.addRoleToUser("Didier", "USER");	
			appUserService.addRoleToUser("Didier", "USER_READER");	
			appUserService.addRoleToUser("Didier", "USER_MANAGER");
			appUserService.addRoleToUser("Didier", "ADMIN");	
			appUserService.addRoleToUser("Didier", "SUPERVISOR");	
			appUserService.addRoleToUser("Delly", "USER");	
			appUserService.addRoleToUser("Delly", "SUPERVISOR");	
			appUserService.addRoleToUser("Declerck", "USER");		
			//Creating a timesheet line
			TimesheetPeriods tp=new TimesheetPeriods();
			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getCurrentPeriod(), employeeService.getEmployeeID("NGOIE"), "", "USAID-ULINZI_2030");
			List<Sheetday>sheetdays2=sheetdayService.getNewTimesheetLine(tp.getCurrentPeriod(), employeeService.getEmployeeID("NGOIE"), "", "ARPA");
			List<Sheetday>sheetdays3=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGUZA"), "", "USAID-EPIC");
			sheetdayService.saveTimesheetLine(sheetdays);
			sheetdayService.saveTimesheetLine(sheetdays2);
			sheetdayService.saveTimesheetLine(sheetdays3);
			TimesheetSaver userSavedSl=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE"), tp.getCurrentPeriod(), false, false, false, false, false);
			TimesheetSaver userSavedS2=new TimesheetSaver(null, employeeService.getEmployeeID("NGUZA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false);
			
			savedSheetLineRepository.save(userSavedSl);
			savedSheetLineRepository.save(userSavedS2);
			
		}; 
	}	 
}
