package com.timesheet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			 
			//Projects
			projectRepository.save(new USAIDProject(null,"USAID ULINZI","Key Population project"));
			projectRepository.save(new USAIDProject(null,"EPIC","Epidemiologic Control"));
			
			//Add employees
			employeeService.addNewEmployee("SEYA", "SEYA", "Francine", "fseya@maisonfaradja.org", Positions.RECEPTIONIST);
			employeeService.addNewEmployee("KYUNGU", "MWANANGOY", "Tresor", "tresorkyungu@maisonfaradja.org", Positions.SITE_SUPERVISOR_KASAJI);
			employeeService.addNewEmployee("MUCHECHE", "MUCHECHE", "Jean-Paul", "jpmucheche@maisonfaradja.org", Positions.SITE_SUPERVISOR_KAPANGA);
			employeeService.addNewEmployee("NGOIE", "MUTOMBO", "Didier", "didierngoie@maisonfaradja.org", Positions.COP);
			employeeService.addNewEmployee("NGOY", "KABWE", "Delly", "dellyngoy@maisonfaradja.org", Positions.DCOP);
			employeeService.addNewEmployee("KASHIKA", "KASHIKA", "Clement", "clementkashika@maisonfaradja.org", Positions.FINANCE_ASSOCIATE);
			employeeService.addNewEmployee("BOMASI", "BOMASI", "Michel", "michelbomasi@maisonfaradja.org", Positions.FINANCE_OFFICER);
			employeeService.addNewEmployee("MUKUMBA", "MUKUMBA", "Curtis", "curtismukumba@maisonfaradja.org", Positions.SSI);
			employeeService.addNewEmployee("KALWA", "KALWA", "Patty", "pattykalwa@maisonfaradja.org", Positions.KPTA);
			employeeService.addNewEmployee("MBUTA", "MBUTA", "Frederic", "fredericmbuta@maisonfaradja.org", Positions.SIM);
			employeeService.addNewEmployee("NOVA", "MUNONGO", "Nova", "novamunongo@maisonfaradja.org", Positions.DAF);
			employeeService.addNewEmployee("MWAMBA", "ILUNGA", "Dyese", "dyese@maisonfaradja.org", Positions.LOG_SUPPLY_CHAIN);
			employeeService.addNewEmployee("ILUNGA", "KABWE", "Vital", "vitalilunga@maisonfaradja.org", Positions.IT_OFFICER_COMM_FP);
//			Add roles
			for(RolesNames roleName:RolesNames.values()) {
				appRoleRepository.save(new Role(null,roleName.name()));
			}
			//define a supervisor
			
			employeeService.setSupervisorToEmployee("KALWA", "KYUNGU");
			employeeService.setSupervisorToEmployee("NGOY", "NOVA");
			employeeService.setSupervisorToEmployee("NGOIE", "NGOY");
			employeeService.setSupervisorToEmployee("NOVA", "MWAMBA");
			employeeService.setSupervisorToEmployee("NOVA", "KASHIKA");
			employeeService.setSupervisorToEmployee("NOVA", "BOMASI");
			employeeService.setSupervisorToEmployee("NOVA", "NGOIE");
			employeeService.setSupervisorToEmployee("KALWA", "KYUNGU");
			employeeService.setSupervisorToEmployee("KALWA", "MUCHECHE");
			employeeService.setSupervisorToEmployee("NGOY", "KALWA");
			employeeService.setSupervisorToEmployee("NGOY", "MBUTA");
			employeeService.setSupervisorToEmployee("MBUTA", "MUKUMBA");
			employeeService.setSupervisorToEmployee("MBUTA", "ILUNGA");
			employeeService.setSupervisorToEmployee("MBUTA", "MUKUMBA");
			employeeService.setSupervisorToEmployee("MWAMBA", "SEYA");
			
//			Add users
			appUserService.saveUser(new AppUser("Didier", "12345", employeeService.getEmployeeID("NGOIE")));
			appUserService.saveUser(new AppUser("Delly", "12345", employeeService.getEmployeeID("NGOY")));
			appUserService.saveUser(new AppUser("Nova", "12345", employeeService.getEmployeeID("NOVA")));
			appUserService.saveUser(new AppUser("Francine", "12345", employeeService.getEmployeeID("SEYA")));
			appUserService.saveUser(new AppUser("Tresor", "12345", employeeService.getEmployeeID("KYUNGU")));
			appUserService.saveUser(new AppUser("Jean-Paul", "12345", employeeService.getEmployeeID("MUCHECHE")));
			appUserService.saveUser(new AppUser("Dyese", "12345", employeeService.getEmployeeID("MWAMBA")));
			appUserService.saveUser(new AppUser("Curtis", "12345", employeeService.getEmployeeID("MUKUMBA")));
			appUserService.saveUser(new AppUser("Vital", "123456", employeeService.getEmployeeID("ILUNGA")));
			appUserService.saveUser(new AppUser("Clement", "12345", employeeService.getEmployeeID("KASHIKA")));
			appUserService.saveUser(new AppUser("Michelle", "12345", employeeService.getEmployeeID("BOMASI")));
//			Add roles to users
			appUserService.addRoleToUser("Didier", "USER");	
			appUserService.addRoleToUser("Didier", "SUPERVISOR");	
			appUserService.addRoleToUser("Didier", "COP");
			appUserService.addRoleToUser("Delly", "USER");	
			appUserService.addRoleToUser("Delly", "SUPERVISOR");	
			appUserService.addRoleToUser("Vital", "USER");	
			appUserService.addRoleToUser("Vital", "ADMIN");
			appUserService.addRoleToUser("Vital", "SUPERVISOR");
			appUserService.addRoleToUser("Vital", "HR");
			appUserService.addRoleToUser("Nova", "USER");	
			appUserService.addRoleToUser("Nova", "SUPERVISOR");	
			appUserService.addRoleToUser("Nova", "DAF");	
			appUserService.addRoleToUser("Curtis", "USER");	
			appUserService.addRoleToUser("Francine", "USER");	
			appUserService.addRoleToUser("Dyese", "USER");	
			appUserService.addRoleToUser("Dyese", "SUPERVISOR");	
			appUserService.addRoleToUser("Michelle", "USER");	
			appUserService.addRoleToUser("Clement", "USER");	
			//Creating a timesheet line
			TimesheetPeriods tp=new TimesheetPeriods();
			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOIE"), "", "USAID ULINZI");
			List<Sheetday>sheetdays2=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOY"), "", "EPIC");
			List<Sheetday>sheetdays3=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("SEYA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays4=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KASHIKA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays5=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("BOMASI"), "", "USAID ULINZI");
			List<Sheetday>sheetdays6=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUKUMBA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays7=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("ILUNGA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays8=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MWAMBA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays9=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NOVA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays10=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KALWA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays11=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUCHECHE"), "", "USAID ULINZI");
			List<Sheetday>sheetdays12=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KYUNGU"), "", "USAID ULINZI");
			sheetdayService.saveTimesheetLine(sheetdays);
			sheetdayService.saveTimesheetLine(sheetdays2);
			sheetdayService.saveTimesheetLine(sheetdays3);
			sheetdayService.saveTimesheetLine(sheetdays4);
			sheetdayService.saveTimesheetLine(sheetdays5);
			sheetdayService.saveTimesheetLine(sheetdays6);
			sheetdayService.saveTimesheetLine(sheetdays7);
			sheetdayService.saveTimesheetLine(sheetdays8);
			sheetdayService.saveTimesheetLine(sheetdays9);
			sheetdayService.saveTimesheetLine(sheetdays10);
			sheetdayService.saveTimesheetLine(sheetdays11);
			sheetdayService.saveTimesheetLine(sheetdays12);
			TimesheetSaver userSavedSl=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS2=new TimesheetSaver(null, employeeService.getEmployeeID("NGOY"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS3=new TimesheetSaver(null, employeeService.getEmployeeID("SEYA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS4=new TimesheetSaver(null, employeeService.getEmployeeID("KASHIKA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS5=new TimesheetSaver(null, employeeService.getEmployeeID("BOMASI"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS6=new TimesheetSaver(null, employeeService.getEmployeeID("MUKUMBA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS7=new TimesheetSaver(null, employeeService.getEmployeeID("ILUNGA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS8=new TimesheetSaver(null, employeeService.getEmployeeID("MWAMBA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS9=new TimesheetSaver(null, employeeService.getEmployeeID("NOVA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS10=new TimesheetSaver(null, employeeService.getEmployeeID("KALWA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS11=new TimesheetSaver(null, employeeService.getEmployeeID("MUCHECHE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS12=new TimesheetSaver(null, employeeService.getEmployeeID("KYUNGU"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			
			savedSheetLineRepository.save(userSavedSl);
			savedSheetLineRepository.save(userSavedS2);
			savedSheetLineRepository.save(userSavedS3);
			savedSheetLineRepository.save(userSavedS4);
			savedSheetLineRepository.save(userSavedS5);
			savedSheetLineRepository.save(userSavedS6);
			savedSheetLineRepository.save(userSavedS7);
			savedSheetLineRepository.save(userSavedS8);
			savedSheetLineRepository.save(userSavedS9);
			savedSheetLineRepository.save(userSavedS10);
			savedSheetLineRepository.save(userSavedS11);
			savedSheetLineRepository.save(userSavedS12);
			*/
		}; 
	}	 
}
