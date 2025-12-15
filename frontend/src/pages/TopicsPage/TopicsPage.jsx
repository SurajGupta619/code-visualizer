import React from 'react'
import Header from '../../components/TopicsComponents/Header/Header'
import TopicCard from '../../components/TopicsComponents/TopicCard/TopicCard'


const TopicsPage = () => {
  return (
    <div className='min-w-full h-screen pb-20 bg-gray-900'>
      <Header />
      <TopicCard />
    </div>
  )
}

export default TopicsPage