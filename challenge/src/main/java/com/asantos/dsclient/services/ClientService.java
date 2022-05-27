package com.asantos.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asantos.dsclient.dto.ClientDTO;
import com.asantos.dsclient.entities.Client;
import com.asantos.dsclient.repositories.ClientRepository;
import com.asantos.dsclient.resources.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	// retrieve all
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllClientsPaged(PageRequest pageRequest) {
		Page<Client> clientList = clientRepository.findAll(pageRequest);
		return clientList.map(client -> new ClientDTO(client));
	}

	// retrieve a client by id
	@Transactional(readOnly = true)
	public ClientDTO findCliendById(Long clientId) {
		Optional<Client> optionalClient = clientRepository.findById(clientId);
		Client client = optionalClient
				.orElseThrow(() -> new ResourceNotFoundException("Resource 'Client' not found!!!"));
		return new ClientDTO(client);
	}

	// add new client
	@Transactional
	public ClientDTO insertNewClient(ClientDTO newClientDto) {
		Client newClient = new Client();
		createClientFromDto(newClientDto, newClient);
		newClient = clientRepository.save(newClient);

		return new ClientDTO(newClient);
	}

	// update existing client
	@Transactional
	public ClientDTO updateClient(Long clientId, ClientDTO clientDto) {
		try {
			@SuppressWarnings("deprecation")
			Client clientToUpdate = clientRepository.getOne(clientId);
			createClientFromDto(clientDto, clientToUpdate);
			clientToUpdate = clientRepository.save(clientToUpdate);
			return new ClientDTO(clientToUpdate);
		} catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Client with id: " + clientId + " not found!");
		}
	}

	//remove a client by his id
	public void deleteClient(Long clientId) {
		try {
			clientRepository.deleteById(clientId);
		} catch (EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Client with id: " + clientId + " not found!");
		}
	}

	// helper method
	private void createClientFromDto(ClientDTO newClientDto, Client newClient) {
		newClient.setName(newClientDto.getName());
		newClient.setCpf(newClientDto.getCpf());
		newClient.setIncome(newClientDto.getIncome());
		newClient.setBirthDate(newClientDto.getBirthDate());
		newClient.setChildren(newClientDto.getChildren());

	}



}
