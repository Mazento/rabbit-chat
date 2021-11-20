import React, { useContext } from "react";
import styled from "styled-components";
import { ChatContext } from "contexts/ChatContext";

const Wrapper = styled.div`
    @media screen and (max-width: 768px) {
      display: none;
    }
  
    display: flex;
    flex-direction: column;
  
    max-width: 100%;
    max-height: 100%;
    width: 15rem;
    height: 20rem;
    padding: 1rem;
    margin-left: 2rem;
    
    background-color: #fff;
    box-shadow: 0 0 1.5rem 0 rgba(173, 179, 181, 0.4), 0 .25rem .3rem 0 rgba(173, 179, 181, 0.1);
    border-radius: 0.5rem;
`

const ListContainer = styled.div`
    overflow: auto;
`

const Title = styled.div`
    display: flex;
    justify-content: center;
  
    font-size: 1.5rem;
    user-select: none;
    color: #6f6f6f;
`

const UserItem = styled.div`
    margin: 0.5rem 0;
    padding: 0.375rem 0.5rem;
    line-height: 1.5;
    
    color: #4a4a4a;
    background-color: #f2f2f2;
    border-radius: 0.375rem;
`

const Separator = styled.div`
    margin-top: 1rem;
    margin-bottom: 1rem;
    
    width: 100%;
    height: 1px;
    border-bottom: solid 1px #e5e5eb;
`

const UsersListContainer = () => {
    const { state: chatState } = useContext(ChatContext);

    return (
        <Wrapper>
            <Title>Users in chat</Title>

            <Separator />

            {chatState.usersList
            && <ListContainer>
                {[...chatState.usersList].map(user => (
                    <UserItem key={user}>{user}</UserItem>
                ))}
            </ListContainer>}
        </Wrapper>
    )
}

export default React.memo(UsersListContainer);
