package com.timesheet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.entities.USAIDProject;
import com.timesheet.repositories.USAIDProjectRepository;

@Service
@Transactional
public class ProjectService {

	@Autowired
	private USAIDProjectRepository projectRepository;
	
	public USAIDProject getProject(Long projectID) {
		return projectRepository.findById(projectID).get();
	}
	public USAIDProject getProject(String projectName) {
		return projectRepository.findByName(projectName);
	}
	public List<USAIDProject>getAllProjects(){
		return projectRepository.findAll();
	}
	public boolean addNewProject(USAIDProject project) {
		if(!project.getName().isEmpty())projectRepository.save(project);
		return true;
	}
	public boolean updateProject(USAIDProject project) {
		USAIDProject usp=projectRepository.findById(project.getId()).get();
		if(!project.getName().isEmpty()) usp.setName(project.getName());
		if(!project.getDescription().isEmpty())usp.setDescription(project.getDescription());
		projectRepository.save(usp);
		return true;
	}
	public boolean deleteProject(Long projectID) {
		projectRepository.deleteById(projectID);
		return true;
	}
}
